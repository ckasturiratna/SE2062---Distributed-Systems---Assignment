package com.example.ds.storage.repair;

public interface Replicator {
    // Stub RPC call to replicate a chunk from source to target
    boolean replicateChunk(String chunkId, String sourceNodeId, String targetNodeId);
}


