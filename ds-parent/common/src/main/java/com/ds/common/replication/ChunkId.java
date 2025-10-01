package com.ds.common.replication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Identifier for a file chunk within the distributed file system.
 */
public final class ChunkId {
    private final String fileId;
    private final int chunkIndex;

    @JsonCreator
    public ChunkId(@JsonProperty("fileId") String fileId,
                   @JsonProperty("chunkIndex") int chunkIndex) {
        if (fileId == null || fileId.isBlank()) {
            throw new IllegalArgumentException("fileId must be non-empty");
        }
        if (chunkIndex < 0) {
            throw new IllegalArgumentException("chunkIndex must be >= 0");
        }
        this.fileId = fileId;
        this.chunkIndex = chunkIndex;
    }

    public String fileId() { return fileId; }
    public int chunkIndex() { return chunkIndex; }

    @Override
    public String toString() {
        return fileId + ":" + chunkIndex;
    }
}


