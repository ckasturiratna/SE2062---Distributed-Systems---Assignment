package com.ds.common.replication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Metadata associated with a chunk replica on a storage node.
 */
public final class ReplicaMetadata {
    private final ChunkId chunkId;
    private final VersionVector versionVector;
    private final OrderedTimestamp timestamp;
    private final String nodeId;

    @JsonCreator
    public ReplicaMetadata(
            @JsonProperty("chunkId") ChunkId chunkId,
            @JsonProperty("versionVector") VersionVector versionVector,
            @JsonProperty("timestamp") OrderedTimestamp timestamp,
            @JsonProperty("nodeId") String nodeId) {
        this.chunkId = Objects.requireNonNull(chunkId);
        this.versionVector = Objects.requireNonNull(versionVector);
        this.timestamp = Objects.requireNonNull(timestamp);
        this.nodeId = Objects.requireNonNull(nodeId);
    }

    public ChunkId chunkId() { return chunkId; }
    public VersionVector versionVector() { return versionVector; }
    public OrderedTimestamp timestamp() { return timestamp; }
    public String nodeId() { return nodeId; }
}


