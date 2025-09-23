package com.ds.common.time;

import java.util.Objects;

/**
 * Composite timestamp combining a Lamport counter with an NTP-adjusted wall clock.
 */
public record OrderedTimestamp(long lamport, long effectiveMillis) implements Comparable<OrderedTimestamp> {

    public OrderedTimestamp {
        if (lamport < 0) {
            throw new IllegalArgumentException("lamport must be non-negative");
        }
    }

    @Override
    public int compareTo(OrderedTimestamp other) {
        int compare = Long.compare(this.lamport, other.lamport);
        if (compare != 0) {
            return compare;
        }
        compare = Long.compare(this.effectiveMillis, other.effectiveMillis);
        if (compare != 0) {
            return compare;
        }
        return Integer.compare(this.hashCode(), other.hashCode());
    }

    /**
     * Creates a timestamp for a local event by ticking the provided Lamport clock.
     */
    public static OrderedTimestamp from(LamportClock lamportClock, TimeSyncService timeSyncService) {
        Objects.requireNonNull(lamportClock, "lamportClock");
        Objects.requireNonNull(timeSyncService, "timeSyncService");
        long lamport = lamportClock.tick();
        long millis = timeSyncService.nowEffectiveMillis();
        return new OrderedTimestamp(lamport, millis);
    }
}
