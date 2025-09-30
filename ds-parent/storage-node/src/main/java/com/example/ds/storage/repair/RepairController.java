package com.example.ds.storage.repair;

import com.example.ds.common.metadata.ChunkMetadata;
import com.example.ds.common.metadata.MetadataClient;
import com.example.ds.storage.membership.MembershipManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RepairController implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(RepairController.class);

    private final MetadataClient metadataClient;
    private final MembershipManager membershipManager;
    private final Replicator replicator;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public RepairController(MetadataClient metadataClient,
                            MembershipManager membershipManager,
                            Replicator replicator) {
        this.metadataClient = metadataClient;
        this.membershipManager = membershipManager;
        this.replicator = replicator;

        this.membershipManager.addListener((nodeId, isUp) -> {
            if (!isUp) {
                executor.submit(() -> handleNodeDown(nodeId));
            } else {
                executor.submit(() -> handleNodeUp(nodeId));
            }
        });
    }

    private void handleNodeDown(String downNodeId) {
        log.info("Handling NodeDown for {}", downNodeId);
        List<ChunkMetadata> affected = metadataClient.getChunksByReplica(downNodeId);
        if (affected.isEmpty()) {
            log.info("No chunks affected by node {} down", downNodeId);
            return;
        }

        Set<String> live = membershipManager.getLiveNodesSnapshot();
        live.remove(downNodeId);

        for (ChunkMetadata chunk : affected) {
            Set<String> currentReplicas = new HashSet<>(chunk.getReplicaNodeIds());
            currentReplicas.remove(downNodeId);

            Optional<String> target = pickTargetNode(live, currentReplicas);
            if (target.isEmpty()) {
                log.warn("No target node available to re-replicate chunk {}", chunk.getChunkId());
                continue;
            }

            String source = currentReplicas.stream().findFirst().orElse(null);
            if (source == null) {
                log.error("Chunk {} has no remaining replicas to copy from", chunk.getChunkId());
                continue;
            }

            String targetNode = target.get();
            log.info("Replicating chunk {} from {} to {}", chunk.getChunkId(), source, targetNode);
            boolean ok = replicator.replicateChunk(chunk.getChunkId(), source, targetNode);
            if (ok) {
                Set<String> newReplicas = new HashSet<>(currentReplicas);
                newReplicas.add(targetNode);
                metadataClient.updateReplicaList(chunk.getChunkId(), newReplicas);
                log.info("Updated replica set for {} -> {}", chunk.getChunkId(), newReplicas);
            } else {
                log.error("Replication failed for chunk {} from {} to {}", chunk.getChunkId(), source, targetNode);
            }
        }
    }

    private void handleNodeUp(String nodeId) {
        log.info("Handling NodeUp for {} (rejoin reconcile)", nodeId);
        // Simple reconcile: ask metadata for chunks that include this node and ensure they exist on others
        List<ChunkMetadata> hosted = metadataClient.getChunksByReplica(nodeId);
        if (hosted.isEmpty()) {
            return;
        }
        Set<String> live = membershipManager.getLiveNodesSnapshot();
        for (ChunkMetadata chunk : hosted) {
            Set<String> replicas = new HashSet<>(chunk.getReplicaNodeIds());
            replicas.retainAll(live);
            if (!replicas.contains(nodeId)) {
                replicas.add(nodeId);
                metadataClient.updateReplicaList(chunk.getChunkId(), replicas);
                log.info("Reconciled metadata for chunk {} to include {}", chunk.getChunkId(), nodeId);
            }
        }
    }

    private Optional<String> pickTargetNode(Set<String> live, Set<String> exclude) {
        List<String> candidates = new ArrayList<>();
        for (String n : live) {
            if (!exclude.contains(n)) {
                candidates.add(n);
            }
        }
        if (candidates.isEmpty()) return Optional.empty();
        return Optional.of(candidates.get((int) (Math.random() * candidates.size())));
    }

    @Override
    public void close() {
        executor.shutdownNow();
    }
}


