package com.ds.common.time;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Configuration for {@link TimeSyncService} describing NTP behaviour and sampling policy.
 *
 * <p>Immutable and thread-safe. Use {@link #builder()} to customize values or {@link #defaults()} for defaults.</p>
 */
public record TimeSyncConfig(List<String> ntpServers,
                             Duration queryInterval,
                             Duration timeout,
                             int maxSamples) {

    private static final List<String> DEFAULT_NTP_SERVERS = List.of("pool.ntp.org", "time.google.com");
    private static final Duration DEFAULT_QUERY_INTERVAL = Duration.ofSeconds(30);
    private static final Duration DEFAULT_TIMEOUT = Duration.ofMillis(1500);
    private static final int DEFAULT_MAX_SAMPLES = 8;

    /**
     * Creates a configuration with sane defaults.
     */
    public static TimeSyncConfig defaults() {
        return builder().build();
    }

    /**
     * Returns a new builder initialised with default values.
     */
    public static Builder builder() {
        return new Builder();
    }

    public TimeSyncConfig {
        Objects.requireNonNull(ntpServers, "ntpServers");
        Objects.requireNonNull(queryInterval, "queryInterval");
        Objects.requireNonNull(timeout, "timeout");
        if (ntpServers.isEmpty()) {
            throw new IllegalArgumentException("ntpServers must not be empty");
        }
        if (queryInterval.isNegative() || queryInterval.isZero()) {
            throw new IllegalArgumentException("queryInterval must be positive");
        }
        if (timeout.isNegative() || timeout.isZero()) {
            throw new IllegalArgumentException("timeout must be positive");
        }
        if (maxSamples <= 0) {
            throw new IllegalArgumentException("maxSamples must be positive");
        }
        ntpServers = List.copyOf(ntpServers);
    }

    /**
     * Builder for {@link TimeSyncConfig}.
     */
    public static final class Builder {
        private final List<String> ntpServers = new ArrayList<>(DEFAULT_NTP_SERVERS);
        private Duration queryInterval = DEFAULT_QUERY_INTERVAL;
        private Duration timeout = DEFAULT_TIMEOUT;
        private int maxSamples = DEFAULT_MAX_SAMPLES;

        private Builder() {
        }

        public Builder ntpServers(List<String> servers) {
            ntpServers.clear();
            ntpServers.addAll(Objects.requireNonNull(servers, "servers"));
            return this;
        }

        public Builder addNtpServer(String server) {
            ntpServers.add(Objects.requireNonNull(server, "server"));
            return this;
        }

        public Builder queryInterval(Duration interval) {
            this.queryInterval = Objects.requireNonNull(interval, "interval");
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = Objects.requireNonNull(timeout, "timeout");
            return this;
        }

        public Builder maxSamples(int maxSamples) {
            this.maxSamples = maxSamples;
            return this;
        }

        public TimeSyncConfig build() {
            return new TimeSyncConfig(ntpServers, queryInterval, timeout, maxSamples);
        }
    }
}
