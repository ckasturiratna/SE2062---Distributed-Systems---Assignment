package com.ds.storage;

import com.ds.common.replication.ChunkId;
import com.ds.common.replication.ReplicaMetadata;
import com.ds.common.replication.VersionVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Schedules replication to maintain N copies and reconciles on rejoin.
 * This is a skeleton; integrate with node health and gRPC calls.
 */
public class RepairManager {
    private final Path dataRoot;
    private final int replicationFactorN;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public RepairManager(Path dataRoot, int replicationFactorN) {
        this.dataRoot = dataRoot;
        this.replicationFactorN = replicationFactorN;
    }

    public void onNodeDown(String nodeId) {
        // scan local inventory and plan replication to healthy nodes
        // placeholder: no-op
    }

    public void onNodeRejoin(String nodeId) {
        // run inventory digest and reconcile by picking newest replica
        // placeholder: no-op
    }

    public List<ChunkId> scanLocalChunks() {
        List<ChunkId> ids = new ArrayList<>();
        Path chunks = dataRoot.resolve("data").resolve("chunks");
        if (Files.notExists(chunks)) return ids;
        try {
            Files.walk(chunks)
                    .filter(Files::isRegularFile)
                    .filter(p -> !p.getFileName().toString().endsWith(".meta.json"))
                    .forEach(p -> {
                        Path fileDir = p.getParent();
                        String fileId = fileDir.getFileName().toString();
                        int chunkIndex = Integer.parseInt(p.getFileName().toString());
                        ids.add(new ChunkId(fileId, chunkIndex));
                    });
        } catch (IOException ignored) {}
        return ids;
    }
}


