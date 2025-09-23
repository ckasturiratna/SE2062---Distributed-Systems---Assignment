package com.ds.common.time;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Periodically samples external NTP servers to derive a robust time offset usable by the cluster.
 *
 * <p>The service never adjusts the system clock; callers can obtain an effective view of time that
 * accounts for the measured offset.</p>
 */
public class TimeSyncService {

    private static final Logger LOGGER = Logger.getLogger(TimeSyncService.class.getName());
    private static final long HEALTHY_JITTER_THRESHOLD_MS = 100L;

    private final TimeSyncConfig config;
    private final NtpClient ntpClient;
    private final ScheduledExecutorService executor;
    private final boolean managedExecutor;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicLong currentOffsetMillis = new AtomicLong(0L);
    private final AtomicLong jitterMillis = new AtomicLong(0L);
    private final AtomicReference<TimeSyncHealth> health;

    private final Object samplesLock = new Object();
    private final Deque<TimeOffsetSample> samples = new ArrayDeque<>();

    private volatile ScheduledFuture<?> scheduledTask;

    public TimeSyncService(TimeSyncConfig config, NtpClient ntpClient) {
        this(config, ntpClient, Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "time-sync-service");
            thread.setDaemon(true);
            return thread;
        }), true);
    }

    TimeSyncService(TimeSyncConfig config, NtpClient ntpClient, ScheduledExecutorService executor) {
        this(config, ntpClient, executor, false);
    }

    private TimeSyncService(TimeSyncConfig config, NtpClient ntpClient, ScheduledExecutorService executor, boolean managedExecutor) {
        this.config = Objects.requireNonNull(config, "config");
        this.ntpClient = Objects.requireNonNull(ntpClient, "ntpClient");
        this.executor = Objects.requireNonNull(executor, "executor");
        this.managedExecutor = managedExecutor;
        this.health = new AtomicReference<>(TimeSyncHealth.unavailable("initialising", Instant.now()));
    }

    /**
     * Starts periodic sampling. Safe to call multiple times.
     */
    public void start() {
        if (running.compareAndSet(false, true)) {
            Duration interval = config.queryInterval();
            scheduledTask = executor.scheduleWithFixedDelay(this::pollOnceSafe, 0L, interval.toMillis(), TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Stops sampling and releases resources.
     */
    public void stop() {
        if (running.compareAndSet(true, false) && scheduledTask != null) {
            scheduledTask.cancel(true);
            scheduledTask = null;
        }
        if (managedExecutor) {
            executor.shutdownNow();
        }
    }

    /**
     * Current best-effort offset in milliseconds, derived from the median of samples.
     */
    public long currentOffsetMillis() {
        return currentOffsetMillis.get();
    }

    /**
     * Median absolute deviation expressed in milliseconds.
     */
    public long jitterMillis() {
        return jitterMillis.get();
    }

    /**
     * Current health snapshot of the synchroniser.
     */
    public TimeSyncHealth health() {
        return health.get();
    }

    /**
     * Effective "now" Instant adjusted by the measured offset.
     */
    public Instant nowEffectiveInstant() {
        return Instant.now().plusMillis(currentOffsetMillis());
    }

    /**
     * Effective epoch milli timestamp adjusted by the measured offset.
     */
    public long nowEffectiveMillis() {
        return Instant.now().toEpochMilli() + currentOffsetMillis();
    }

    private void pollOnceSafe() {
        try {
            pollOnce();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Unexpected error while polling NTP servers", ex);
        }
    }

    void pollOnce() {
        List<String> servers = config.ntpServers();
        boolean anySuccess = false;
        for (String server : servers) {
            Optional<TimeOffsetSample> sample = ntpClient.fetchOffset(server, config.timeout());
            if (sample.isPresent()) {
                recordSample(sample.get());
                anySuccess = true;
            } else {
                LOGGER.log(Level.WARNING, "Failed to obtain NTP sample from {0}", server);
            }
        }
        if (!anySuccess) {
            updateHealth();
        }
    }

    void recordSample(TimeOffsetSample sample) {
        Objects.requireNonNull(sample, "sample");
        synchronized (samplesLock) {
            samples.addLast(sample);
            while (samples.size() > config.maxSamples()) {
                samples.removeFirst();
            }
            recomputeStats();
        }
    }

    private void recomputeStats() {
        List<TimeOffsetSample> snapshot = new ArrayList<>(samples);
        if (snapshot.isEmpty()) {
            currentOffsetMillis.set(0L);
            jitterMillis.set(0L);
            updateHealthWith(0, 0L);
            return;
        }

        List<Long> offsets = snapshot.stream()
                .map(TimeOffsetSample::offsetMillis)
                .sorted()
                .toList();
        long medianOffset = median(offsets);
        currentOffsetMillis.set(medianOffset);

        long newJitter;
        if (offsets.size() == 1) {
            newJitter = 0L;
        } else {
            List<Long> deviations = offsets.stream()
                    .map(offset -> Math.abs(offset - medianOffset))
                    .sorted()
                    .toList();
            newJitter = median(deviations);
        }
        jitterMillis.set(newJitter);
        updateHealthWith(snapshot.size(), newJitter);
    }

    private void updateHealth() {
        synchronized (samplesLock) {
            updateHealthWith(samples.size(), jitterMillis.get());
        }
    }

    private void updateHealthWith(int sampleCount, long jitter) {
        TimeSyncHealth.Status status;
        String reason;
        if (sampleCount >= 3 && jitter < HEALTHY_JITTER_THRESHOLD_MS) {
            status = TimeSyncHealth.Status.HEALTHY;
            reason = "offset stable";
        } else if (sampleCount >= 1) {
            status = TimeSyncHealth.Status.DEGRADED;
            reason = sampleCount < 3 ? "insufficient samples" : "high jitter";
        } else {
            status = TimeSyncHealth.Status.UNAVAILABLE;
            reason = "no samples yet";
        }
        health.set(new TimeSyncHealth(status, reason, Instant.now()));
    }

    private long median(List<Long> sortedValues) {
        if (sortedValues.isEmpty()) {
            return 0L;
        }
        int size = sortedValues.size();
        if (size % 2 == 1) {
            return sortedValues.get(size / 2);
        }
        long lower = sortedValues.get(size / 2 - 1);
        long upper = sortedValues.get(size / 2);
        return lower + (upper - lower) / 2;
    }
}
