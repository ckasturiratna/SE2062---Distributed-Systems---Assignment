package com.ds.common.time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LamportClockTest {

    @Test
    void tickAdvancesMonotonically() {
        LamportClock clock = new LamportClock();
        assertEquals(0L, clock.current());
        long first = clock.tick();
        long second = clock.tick();

        assertTrue(first > 0, "First tick should be positive");
        assertTrue(second > first, "Lamport clock must be strictly monotonic on local ticks");
    }

    @Test
    void updateAdoptsMaximumAndAdvances() {
        LamportClock clock = new LamportClock();
        clock.tick();
        long updated = clock.update(10);

        assertEquals(11L, updated);
        assertEquals(updated, clock.current());

        long next = clock.tick();
        assertEquals(12L, next);
    }

    @Test
    void rejectsNegativeValues() {
        assertThrows(IllegalArgumentException.class, () -> new LamportClock(-1));
        LamportClock clock = new LamportClock();
        assertThrows(IllegalArgumentException.class, () -> clock.update(-5));
    }
}
