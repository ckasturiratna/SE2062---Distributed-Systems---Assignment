package com.ds.gateway.replication;

import com.ds.common.replication.ChunkId;
import com.ds.common.replication.OrderedTimestamp;
import com.ds.common.replication.ReplicaMetadata;
import com.ds.common.replication.VersionVector;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Quorum client to coordinate read/write operations to storage replicas.
 */
public class QuorumClient {
    private final int replicationFactorN;
    private final int readQuorumR;
    private final int writeQuorumW;
    private final ConflictResolver resolver;

    // simple counters for metrics
    private final ConcurrentHashMap<String, Long> metrics = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public QuorumClient() {
        this(3, 2, 2, new ConflictResolver());
    }

    public QuorumClient(int n, int r, int w, ConflictResolver resolver) {
        if (r + w <= n) {
            throw new IllegalArgumentException("Quorum rule violated: R + W must be > N");
        }
        this.replicationFactorN = n;
        this.readQuorumR = r;
        this.writeQuorumW = w;
        this.resolver = Objects.requireNonNull(resolver);
    }

    public void write(ChunkId chunkId, byte[] data, List<String> replicaNodeIds) {
        Objects.requireNonNull(chunkId);
        Objects.requireNonNull(data);
        Objects.requireNonNull(replicaNodeIds);
        if (replicaNodeIds.size() < replicationFactorN) {
            throw new IllegalArgumentException("insufficient replicas provided");
        }

        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        for (String nodeId : replicaNodeIds) {
            futures.add(CompletableFuture.supplyAsync(() -> sendPut(nodeId, chunkId, data), executor));
        }

        int successes = waitForAtLeast(futures, writeQuorumW);
        if (successes < writeQuorumW) {
            increment("quorum.write.failure");
            throw new RuntimeException("Failed to meet write quorum W=" + writeQuorumW);
        }
        increment("quorum.write.success");
    }

    public ReadResult read(ChunkId chunkId, List<String> replicaNodeIds) {
        Objects.requireNonNull(chunkId);
        Objects.requireNonNull(replicaNodeIds);
        if (replicaNodeIds.isEmpty()) throw new IllegalArgumentException("replicas required");

        List<CompletableFuture<Replica>> futures = new ArrayList<>();
        for (int i = 0; i < Math.min(readQuorumR, replicaNodeIds.size()); i++) {
            String nodeId = replicaNodeIds.get(i);
            futures.add(CompletableFuture.supplyAsync(() -> sendGet(nodeId, chunkId), executor));
        }

        List<Replica> responses = waitForAllSuccessful(futures);
        if (responses.isEmpty()) {
            increment("quorum.read.failure");
            throw new RuntimeException("No replica responded successfully");
        }

        List<ReplicaMetadata> metas = responses.stream().map(r -> r.metadata).collect(Collectors.toList());
        ReplicaMetadata winner = resolver.resolve(metas);

        // schedule read-repair for stale replicas
        for (Replica r : responses) {
            if (r.metadata != winner) {
                increment("repair.scheduled");
                CompletableFuture.runAsync(() -> sendPut(r.nodeId, chunkId, winnerPayload(winner)), executor);
            }
        }

        increment("quorum.read.success");
        return new ReadResult(winnerPayload(winner), winner);
    }

    private byte[] winnerPayload(ReplicaMetadata winner) {
        // In a real system, we'd fetch the payload from the node that had the winner metadata
        // Here, return empty to keep the scaffold independent of transport
        return new byte[0];
    }

    private int waitForAtLeast(List<CompletableFuture<Boolean>> futures, int atLeast) {
        int successes = 0;
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
        List<CompletableFuture<Boolean>> remaining = new ArrayList<>(futures);
        while (!remaining.isEmpty() && successes < atLeast && System.nanoTime() < deadline) {
            remaining.removeIf(f -> {
                if (f.isDone()) {
                    try {
                        if (Boolean.TRUE.equals(f.join())) {
                            successes++;
                        }
                    } catch (Exception ignored) {}
                    return true;
                }
                return false;
            });
            try { Thread.sleep(5); } catch (InterruptedException ignored) {}
        }
        return successes;
    }

    private <T> List<T> waitForAllSuccessful(List<CompletableFuture<T>> futures) {
        List<T> results = new ArrayList<>();
        for (CompletableFuture<T> f : futures) {
            try {
                results.add(f.join());
            } catch (Exception ignored) {}
        }
        return results;
    }

    // Transport stubs (to be wired to gRPC clients)
    protected boolean sendPut(String nodeId, ChunkId chunkId, byte[] data) {
        // placeholder: pretend success and local version bump
        return true;
    }

    protected Replica sendGet(String nodeId, ChunkId chunkId) {
        // placeholder returning empty payload with default metadata
        VersionVector vv = new VersionVector();
        vv.increment(nodeId);
        ReplicaMetadata md = new ReplicaMetadata(
                chunkId,
                vv,
                new OrderedTimestamp(1, Instant.now(), nodeId),
                nodeId
        );
        return new Replica(nodeId, new byte[0], md);
    }

    private void increment(String metric) {
        metrics.merge(metric, 1L, Long::sum);
    }

    public long metric(String metric) {
        return metrics.getOrDefault(metric, 0L);
    }

    public static final class ReadResult {
        public final byte[] data;
        public final ReplicaMetadata metadata;
        public ReadResult(byte[] data, ReplicaMetadata metadata) {
            this.data = data;
            this.metadata = metadata;
        }
    }

    protected static class Replica {
        public final String nodeId;
        public final byte[] data;
        public final ReplicaMetadata metadata;
        public Replica(String nodeId, byte[] data, ReplicaMetadata metadata) {
            this.nodeId = nodeId;
            this.data = data;
            this.metadata = metadata;
        }
    }
}


