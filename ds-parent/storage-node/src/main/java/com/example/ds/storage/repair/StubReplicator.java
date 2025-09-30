package com.example.ds.storage.repair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StubReplicator implements Replicator {
    private static final Logger log = LoggerFactory.getLogger(StubReplicator.class);

    @Override
    public boolean replicateChunk(String chunkId, String sourceNodeId, String targetNodeId) {
        log.info("Stub ReplicateChunk: chunk={}, from={}, to={}", chunkId, sourceNodeId, targetNodeId);
        // Simulate success
        return true;
    }
}


