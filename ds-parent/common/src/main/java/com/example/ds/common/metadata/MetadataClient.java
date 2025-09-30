package com.example.ds.common.metadata;

import java.util.List;
import java.util.Set;

public interface MetadataClient {
    List<ChunkMetadata> getChunksByReplica(String replicaNodeId);

    void updateReplicaList(String chunkId, Set<String> newReplicaSet);
}


