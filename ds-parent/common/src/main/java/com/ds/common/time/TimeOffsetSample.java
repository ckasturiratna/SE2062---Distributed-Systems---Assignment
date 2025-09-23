package com.ds.common.time;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a single NTP offset measurement.
 */
public record TimeOffsetSample(long offsetMillis,
                               long rttMillis,
                               Instant at,
                               String server) {

    public TimeOffsetSample {
        Objects.requireNonNull(at, "at");
        Objects.requireNonNull(server, "server");
        if (rttMillis < 0) {
            throw new IllegalArgumentException("rttMillis must be non-negative");
        }
    }
}
