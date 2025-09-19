package com.distributed.filestorage.storage.service;

import com.distributed.filestorage.common.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Local file storage service for chunk management
 */
@Service
public class LocalStorageService {
    private static final Logger logger = LoggerFactory.getLogger(LocalStorageService.class);
    
    @Value("${storage.data.directory:./data}")
    private String dataDirectory;
    
    @Value("${storage.node.id}")
    private String nodeId;
    
    private Path storagePath;
    private final Map<String, ChunkInfo> chunkIndex = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void initialize() throws IOException {
        storagePath = Paths.get(dataDirectory, nodeId);
        Files.createDirectories(storagePath);
        
        logger.info("Initialized local storage at: {}", storagePath.toAbsolutePath());
        
        // Load existing chunks
        loadExistingChunks();
    }
    
    private void loadExistingChunks() throws IOException {
        if (!Files.exists(storagePath)) {
            return;
        }
        
        Files.walk(storagePath)
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().endsWith(".chunk"))
                .forEach(this::loadChunkInfo);
        
        logger.info("Loaded {} existing chunks", chunkIndex.size());
    }
    
    private void loadChunkInfo(Path chunkFile) {
        try {
            String fileName = chunkFile.getFileName().toString();
            String[] parts = fileName.replace(".chunk", "").split("_");
            
            if (parts.length == 2) {
                String fileId = parts[0];
                int chunkIndexValue = Integer.parseInt(parts[1]);
                
                byte[] data = Files.readAllBytes(chunkFile);
                String checksum = CommonUtils.calculateChecksum(data);
                
                ChunkInfo info = new ChunkInfo(fileId, chunkIndexValue, data.length, checksum, 
                        Files.getLastModifiedTime(chunkFile).toMillis());
                
                this.chunkIndex.put(generateChunkKey(fileId, chunkIndexValue), info);
            }
        } catch (Exception e) {
            logger.error("Error loading chunk info from: {}", chunkFile, e);
        }
    }
    
    public String storeChunk(String fileId, int chunkIndex, byte[] data) throws IOException {
        String checksum = CommonUtils.calculateChecksum(data);
        String chunkKey = generateChunkKey(fileId, chunkIndex);
        Path chunkFile = getChunkPath(fileId, chunkIndex);
        
        // Create parent directories if they don't exist
        Files.createDirectories(chunkFile.getParent());
        
        // Write chunk data
        Files.write(chunkFile, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        
        // Update chunk index
        ChunkInfo info = new ChunkInfo(fileId, chunkIndex, data.length, checksum, System.currentTimeMillis());
        this.chunkIndex.put(chunkKey, info);
        
        logger.debug("Stored chunk: {} ({} bytes)", chunkKey, data.length);
        return checksum;
    }
    
    public byte[] retrieveChunk(String fileId, int chunkIndex) throws IOException {
        String chunkKey = generateChunkKey(fileId, chunkIndex);
        ChunkInfo info = this.chunkIndex.get(chunkKey);
        
        if (info == null) {
            throw new IOException("Chunk not found: " + chunkKey);
        }
        
        Path chunkFile = getChunkPath(fileId, chunkIndex);
        if (!Files.exists(chunkFile)) {
            // Remove from index if file doesn't exist
            this.chunkIndex.remove(chunkKey);
            throw new IOException("Chunk file not found: " + chunkFile);
        }
        
        byte[] data = Files.readAllBytes(chunkFile);
        
        // Verify checksum
        String actualChecksum = CommonUtils.calculateChecksum(data);
        if (!actualChecksum.equals(info.checksum)) {
            logger.error("Checksum mismatch for chunk: {}. Expected: {}, Actual: {}", 
                    chunkKey, info.checksum, actualChecksum);
            throw new IOException("Chunk data corrupted: " + chunkKey);
        }
        
        logger.debug("Retrieved chunk: {} ({} bytes)", chunkKey, data.length);
        return data;
    }
    
    public boolean deleteChunk(String fileId, int chunkIndex) throws IOException {
        String chunkKey = generateChunkKey(fileId, chunkIndex);
        Path chunkFile = getChunkPath(fileId, chunkIndex);
        
        boolean deleted = false;
        if (Files.exists(chunkFile)) {
            Files.delete(chunkFile);
            deleted = true;
        }
        
        ChunkInfo removed = this.chunkIndex.remove(chunkKey);
        
        logger.debug("Deleted chunk: {} (existed: {})", chunkKey, removed != null);
        return deleted || removed != null;
    }
    
    public ChunkInfo getChunkInfo(String fileId, int chunkIndex) {
        String chunkKey = generateChunkKey(fileId, chunkIndex);
        return this.chunkIndex.get(chunkKey);
    }
    
    public boolean hasChunk(String fileId, int chunkIndex) {
        return this.chunkIndex.containsKey(generateChunkKey(fileId, chunkIndex));
    }
    
    public Map<String, ChunkInfo> getAllChunks() {
        return Map.copyOf(this.chunkIndex);
    }
    
    public long getTotalStorageSize() {
        return this.chunkIndex.values().stream()
                .mapToLong(info -> info.size)
                .sum();
    }
    
    public long getAvailableSpace() throws IOException {
        return Files.getFileStore(storagePath).getUsableSpace();
    }
    
    private String generateChunkKey(String fileId, int chunkIndex) {
        return fileId + "_" + chunkIndex;
    }
    
    private Path getChunkPath(String fileId, int chunkIndex) {
        return storagePath.resolve(fileId + "_" + chunkIndex + ".chunk");
    }
    
    public static class ChunkInfo {
        public final String fileId;
        public final int chunkIndex;
        public final long size;
        public final String checksum;
        public final long createdAt;
        
        public ChunkInfo(String fileId, int chunkIndex, long size, String checksum, long createdAt) {
            this.fileId = fileId;
            this.chunkIndex = chunkIndex;
            this.size = size;
            this.checksum = checksum;
            this.createdAt = createdAt;
        }
        
        @Override
        public String toString() {
            return "ChunkInfo{" +
                    "fileId='" + fileId + '\'' +
                    ", chunkIndex=" + chunkIndex +
                    ", size=" + size +
                    ", checksum='" + checksum + '\'' +
                    ", createdAt=" + createdAt +
                    '}';
        }
    }
}