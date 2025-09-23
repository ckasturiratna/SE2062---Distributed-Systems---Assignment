package com.ds.common.time;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("Requires external network access to reach public NTP servers")
class NtpClientIT {

    @Test
    void fetchesOffsetFromPool() {
        NtpClient client = new NtpClient();
        TimeOffsetSample sample = client.fetchOffset("pool.ntp.org", Duration.ofSeconds(1))
                .orElseThrow(() -> new AssertionError("Expected an NTP sample"));

        assertTrue(Math.abs(sample.offsetMillis()) < 1000, "Offset should be plausible");
    }
}
