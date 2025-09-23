package com.ds.common.time;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable health snapshot for {@link TimeSyncService}.
 */
public record TimeSyncHealth(Status status, String reason, Instant lastUpdated) {

    public TimeSyncHealth {
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(reason, "reason");
        Objects.requireNonNull(lastUpdated, "lastUpdated");
    }

    /**
     * High-level state of the time synchronisation subsystem.
     */
    public enum Status {
        HEALTHY,
        DEGRADED,
        UNAVAILABLE
    }

    public static TimeSyncHealth healthy(String reason, Instant lastUpdated) {
        return new TimeSyncHealth(Status.HEALTHY, reason, lastUpdated);
    }

    public static TimeSyncHealth degraded(String reason, Instant lastUpdated) {
        return new TimeSyncHealth(Status.DEGRADED, reason, lastUpdated);
    }

    public static TimeSyncHealth unavailable(String reason, Instant lastUpdated) {
        return new TimeSyncHealth(Status.UNAVAILABLE, reason, lastUpdated);
    }
}
