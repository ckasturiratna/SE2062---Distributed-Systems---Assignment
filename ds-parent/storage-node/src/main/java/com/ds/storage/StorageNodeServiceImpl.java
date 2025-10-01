package com.ds.storage;

import com.ds.common.replication.ChunkId;
import com.ds.common.replication.OrderedTimestamp;
import com.ds.common.replication.ReplicaMetadata;
import com.ds.common.replication.VersionVector;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Basic storage node service implementation with filesystem-backed chunk storage.
 * This is a scaffold that should be wired to gRPC generated stubs.
 */
public class StorageNodeServiceImpl {
    private final Path dataRoot;
    private final String nodeId;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ConcurrentHashMap<String, VersionVector> localCounters = new ConcurrentHashMap<>();

    public StorageNodeServiceImpl(Path dataRoot, String nodeId) {
        this.dataRoot = dataRoot;
        this.nodeId = nodeId;
    }

    public ReplicaMetadata putChunk(ChunkId id, byte[] payload, ReplicaMetadata clientMetadata) {
        try {
            Path chunkPath = chunkPath(id);
            Files.createDirectories(chunkPath.getParent());
            Files.write(chunkPath, payload, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            VersionVector vv = localCounters.computeIfAbsent(id.toString(), k -> new VersionVector());
            vv.increment(nodeId);
            ReplicaMetadata md = new ReplicaMetadata(id, vv, new OrderedTimestamp(vv.get(nodeId), Instant.now(), nodeId), nodeId);
            writeMetadata(id, md);
            return md;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getChunk(ChunkId id) {
        try {
            Path chunkPath = chunkPath(id);
            if (Files.notExists(chunkPath)) return new byte[0];
            return Files.readAllBytes(chunkPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ReplicaMetadata readMetadata(ChunkId id) {
        try {
            Path metaPath = metadataPath(id);
            if (Files.notExists(metaPath)) {
                VersionVector vv = new VersionVector();
                return new ReplicaMetadata(id, vv, new OrderedTimestamp(0, Instant.EPOCH, nodeId), nodeId);
            }
            return mapper.readValue(Files.readString(metaPath), ReplicaMetadata.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeMetadata(ChunkId id, ReplicaMetadata md) throws IOException {
        Path metaPath = metadataPath(id);
        Files.createDirectories(metaPath.getParent());
        Files.writeString(metaPath, mapper.writeValueAsString(md), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private Path chunkPath(ChunkId id) {
        return dataRoot.resolve("data").resolve("chunks").resolve(id.fileId()).resolve(String.valueOf(id.chunkIndex()));
    }

    private Path metadataPath(ChunkId id) {
        return dataRoot.resolve("data").resolve("chunks").resolve(id.fileId()).resolve(id.chunkIndex() + ".meta.json");
    }
}


