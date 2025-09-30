package com.example.ds.storage;

import com.example.ds.common.metadata.ChunkMetadata;
import com.example.ds.common.metadata.MetadataClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryMetadataClient implements MetadataClient {
    private final Map<String, Set<String>> chunkToReplicas = new ConcurrentHashMap<>();

    @Override
    public List<ChunkMetadata> getChunksByReplica(String replicaNodeId) {
        List<ChunkMetadata> result = new ArrayList<>();
        for (Map.Entry<String, Set<String>> e : chunkToReplicas.entrySet()) {
            if (e.getValue().contains(replicaNodeId)) {
                result.add(new ChunkMetadata(e.getKey(), e.getValue()));
            }
        }
        return result;
    }

    @Override
    public void updateReplicaList(String chunkId, Set<String> newReplicaSet) {
        chunkToReplicas.put(chunkId, new HashSet<>(newReplicaSet));
    }

    public void put(String chunkId, Set<String> replicas) {
        chunkToReplicas.put(chunkId, new HashSet<>(replicas));
    }

    public static InMemoryMetadataClient defaultDemo() {
        InMemoryMetadataClient c = new InMemoryMetadataClient();
        c.put("chunk-A", setOf("node-1", "node-2"));
        c.put("chunk-B", setOf("node-2", "node-3"));
        c.put("chunk-C", setOf("node-1", "node-3"));
        return c;
    }

    private static Set<String> setOf(String... vals) {
        Set<String> s = new HashSet<>();
        Collections.addAll(s, vals);
        return s;
    }
}


