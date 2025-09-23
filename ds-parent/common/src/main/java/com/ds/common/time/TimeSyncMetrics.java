package com.ds.common.time;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Simple holder for metric suppliers so external systems can bind to monitoring frameworks.
 */
public record TimeSyncMetrics(Supplier<Long> offsetMillis,
                              Supplier<Long> jitterMillis,
                              Supplier<Integer> healthCode) {

    public TimeSyncMetrics {
        Objects.requireNonNull(offsetMillis, "offsetMillis");
        Objects.requireNonNull(jitterMillis, "jitterMillis");
        Objects.requireNonNull(healthCode, "healthCode");
    }

    /**
     * Creates metrics backed by the provided {@link TimeSyncService}.
     */
    public static TimeSyncMetrics from(TimeSyncService service) {
        Objects.requireNonNull(service, "service");
        return new TimeSyncMetrics(service::currentOffsetMillis, service::jitterMillis, () -> toCode(service.health()));
    }

    private static int toCode(TimeSyncHealth health) {
        return switch (health.status()) {
            case HEALTHY -> 0;
            case DEGRADED -> 1;
            case UNAVAILABLE -> 2;
        };
    }
}
