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

class TimeSyncServiceTest {

    private ScheduledExecutorService executor;

    @BeforeEach
    void setUp() {
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    @AfterEach
    void tearDown() {
        executor.shutdownNow();
    }

    @Test
    void computesMedianOffsetAndJitter() {
        TimeSyncService service = new TimeSyncService(configWithServers(List.of("test")), new FakeNtpClient(), executor);

        service.recordSample(sample(10));
        service.recordSample(sample(12));
        service.recordSample(sample(20));
        service.recordSample(sample(11));
        service.recordSample(sample(9));

        assertEquals(11L, service.currentOffsetMillis());
        assertEquals(1L, service.jitterMillis());
        assertEquals(TimeSyncHealth.Status.HEALTHY, service.health().status());
        assertEquals("offset stable", service.health().reason());
    }

    @Test
    void healthDegradesWithInsufficientSamples() {
        TimeSyncService service = new TimeSyncService(configWithServers(List.of("test")), new FakeNtpClient(), executor);
        service.recordSample(sample(42));

        assertEquals(TimeSyncHealth.Status.DEGRADED, service.health().status());
        assertEquals("insufficient samples", service.health().reason());
    }

    @Test
    void healthDegradesWithHighJitter() {
        TimeSyncService service = new TimeSyncService(configWithServers(List.of("test")), new FakeNtpClient(), executor);

        service.recordSample(sample(0));
        service.recordSample(sample(1000));
        service.recordSample(sample(-1000));
        service.recordSample(sample(0));

        assertEquals(TimeSyncHealth.Status.DEGRADED, service.health().status());
        assertEquals("high jitter", service.health().reason());
        assertTrue(service.jitterMillis() >= 100L);
    }

    @Test
    void effectiveTimeAppliesOffset() {
        TimeSyncService service = new TimeSyncService(configWithServers(List.of("test")), new FakeNtpClient(), executor);
        service.recordSample(sample(200));

        long before = Instant.now().toEpochMilli();
        long effective = service.nowEffectiveMillis();

        long delta = effective - before;
        assertTrue(delta >= 150 && delta <= 250, "Effective millis should apply offset within tolerance");
    }

    @Test
    void pollOnceUsesClientResponses() {
        FakeNtpClient client = new FakeNtpClient();
        TimeSyncService service = new TimeSyncService(configWithServers(List.of("a", "b")), client, executor);

        client.whenServer("a", sample(120));
        client.whenServer("b", null);

        service.pollOnce();

        assertEquals(120L, service.currentOffsetMillis());
        assertEquals(TimeSyncHealth.Status.DEGRADED, service.health().status());
    }

    private static TimeOffsetSample sample(long offset) {
        return new TimeOffsetSample(offset, 5L, Instant.now(), "test");
    }

    private static TimeSyncConfig configWithServers(List<String> servers) {
        return TimeSyncConfig.builder()
                .ntpServers(servers)
                .queryInterval(Duration.ofSeconds(1))
                .timeout(Duration.ofMillis(500))
                .maxSamples(8)
                .build();
    }

    private static final class FakeNtpClient extends NtpClient {
        private final java.util.Map<String, Optional<TimeOffsetSample>> responses = new java.util.concurrent.ConcurrentHashMap<>();

        void whenServer(String server, TimeOffsetSample sample) {
            responses.put(server, Optional.ofNullable(sample));
        }

        @Override
        public Optional<TimeOffsetSample> fetchOffset(String server, Duration timeout) {
            return responses.getOrDefault(server, Optional.empty());
        }
    }
}
