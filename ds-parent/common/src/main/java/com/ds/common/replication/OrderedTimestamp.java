package com.ds.common.replication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

/**
 * Ordered timestamp with monotonic ordering for tie-breaks (Lamport/NTP hybrid).
 * If two timestamps have the same logical time, breaks ties using nodeId lexicographically.
 */
public final class OrderedTimestamp implements Comparable<OrderedTimestamp> {
    private final long logicalTime;
    private final Instant wallTime;
    private final String nodeId;

    @JsonCreator
    public OrderedTimestamp(
            @JsonProperty("logicalTime") long logicalTime,
            @JsonProperty("wallTime") Instant wallTime,
            @JsonProperty("nodeId") String nodeId) {
        this.logicalTime = logicalTime;
        this.wallTime = Objects.requireNonNull(wallTime);
        this.nodeId = Objects.requireNonNull(nodeId);
    }

    public long logicalTime() { return logicalTime; }
    public Instant wallTime() { return wallTime; }
    public String nodeId() { return nodeId; }

    @Override
    public int compareTo(OrderedTimestamp o) {
        int cmp = Long.compare(this.logicalTime, o.logicalTime);
        if (cmp != 0) return cmp;
        cmp = this.wallTime.compareTo(o.wallTime);
        if (cmp != 0) return cmp;
        return this.nodeId.compareTo(o.nodeId);
    }

    @Override
    public String toString() {
        return "OrderedTimestamp{" +
                "logicalTime=" + logicalTime +
                ", wallTime=" + wallTime +
                ", nodeId='" + nodeId + '\'' +
                '}';
    }
}


