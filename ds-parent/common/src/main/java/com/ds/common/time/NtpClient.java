package com.ds.common.time;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

/**
 * Lightweight wrapper around {@link NTPUDPClient} providing safe offset sampling.
 */
public class NtpClient {

    private static final Logger LOGGER = Logger.getLogger(NtpClient.class.getName());

    /**
     * Fetches an offset sample from the given server.
     *
     * @param server  hostname or IP address of the NTP server
     * @param timeout timeout for the request
     * @return optional sample, empty if the request failed or did not produce a valid measurement
     */
    public Optional<TimeOffsetSample> fetchOffset(String server, Duration timeout) {
        Objects.requireNonNull(server, "server");
        Objects.requireNonNull(timeout, "timeout");
        if (timeout.isNegative() || timeout.isZero()) {
            throw new IllegalArgumentException("timeout must be positive");
        }

        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout((int) Math.min(Integer.MAX_VALUE, timeout.toMillis()));
        client.setVersion(4);
        try {
            client.open();
            InetAddress address = InetAddress.getByName(server);
            TimeInfo timeInfo = client.getTime(address);
            timeInfo.computeDetails();
            Long offset = timeInfo.getOffset();
            Long delay = timeInfo.getDelay();
            if (offset == null || delay == null) {
                return Optional.empty();
            }
            return Optional.of(new TimeOffsetSample(offset, delay, Instant.now(), server));
        } catch (IOException e) {
            LOGGER.log(Level.FINE, () -> "NTP fetch failed for server " + server + ": " + e.getMessage());
            return Optional.empty();
        } finally {
            client.close();
        }
    }
}
