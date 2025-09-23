package com.ds.common.time;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe Lamport logical clock implementation.
 */
public final class LamportClock {

    private final AtomicLong counter;

    /**
     * Creates a clock starting at zero.
     */
    public LamportClock() {
        this(0L);
    }

    /**
     * Creates a clock initialised to the provided value.
     */
    public LamportClock(long initialValue) {
        if (initialValue < 0) {
            throw new IllegalArgumentException("initialValue must be non-negative");
        }
        this.counter = new AtomicLong(initialValue);
    }

    /**
     * Returns the current logical time without mutation.
     */
    public long current() {
        return counter.get();
    }

    /**
     * Advances the clock for a local event and returns the new value.
     */
    public long tick() {
        return counter.incrementAndGet();
    }

    /**
     * Updates the clock based on a received timestamp and returns the new value.
     */
    public long update(long seen) {
        if (seen < 0) {
            throw new IllegalArgumentException("seen must be non-negative");
        }
        return counter.updateAndGet(current -> Math.max(current, seen) + 1);
    }
}
