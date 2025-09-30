package com.example.ds.common.metadata;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ChunkMetadata {
    private final String chunkId;
    private final Set<String> replicaNodeIds;

    public ChunkMetadata(String chunkId, Set<String> replicaNodeIds) {
        this.chunkId = Objects.requireNonNull(chunkId, "chunkId");
        this.replicaNodeIds = Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(replicaNodeIds, "replicaNodeIds")));
    }

    public String getChunkId() {
        return chunkId;
    }

    public Set<String> getReplicaNodeIds() {
        return replicaNodeIds;
    }

    @Override
    public String toString() {
        return "ChunkMetadata{" +
                "chunkId='" + chunkId + '\'' +
                ", replicas=" + replicaNodeIds +
                '}';
    }
}


