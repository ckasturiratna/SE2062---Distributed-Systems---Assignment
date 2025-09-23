package com.ds.common.time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeSyncExamplesTest {

    private ScheduledExecutorService executor;
    private TimeSyncService service;

    @BeforeEach
    void setUp() {
        executor = Executors.newSingleThreadScheduledExecutor();
        service = new TimeSyncService(TimeSyncConfig.builder()
                .ntpServers(List.of("example"))
                .queryInterval(Duration.ofSeconds(5))
                .timeout(Duration.ofMillis(500))
                .maxSamples(4)
                .build(),
            new NoopNtpClient(),
            executor);
        service.recordSample(new TimeOffsetSample(0L, 1L, Instant.now(), "example"));
    }

    @AfterEach
    void tearDown() {
        executor.shutdownNow();
    }

    @Test
    void stampingWriteExample() {
        LamportClock clock = new LamportClock();

        OrderedTimestamp ts = OrderedTimestamp.from(clock, service);

        assertEquals(1L, ts.lamport());
    }

    @Test
    void metadataServiceUpdateExample() {
        LamportClock clock = new LamportClock();
        OrderedTimestamp incoming = new OrderedTimestamp(41L, service.nowEffectiveMillis());

        long merged = clock.update(incoming.lamport());
        assertTrue(merged > incoming.lamport());

        OrderedTimestamp newTs = OrderedTimestamp.from(clock, service);
        assertTrue(newTs.lamport() > incoming.lamport());
    }

    private static final class NoopNtpClient extends NtpClient {
        @Override
        public Optional<TimeOffsetSample> fetchOffset(String server, Duration timeout) {
            return Optional.empty();
        }
    }
}
