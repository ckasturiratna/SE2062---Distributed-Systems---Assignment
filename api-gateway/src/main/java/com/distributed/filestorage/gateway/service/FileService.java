package com.distributed.filestorage.gateway.service;

import com.distributed.filestorage.common.dto.FileMetadata;
import com.distributed.filestorage.common.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Service for managing file operations
 * This is a simplified implementation for the initial version
 */
@Service
public class FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    
    // Temporary in-memory storage for demonstration
    private final Map<String, FileData> fileStorage = new ConcurrentHashMap<>();
    
    public String storeFile(MultipartFile file, int replicationFactor) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }
        
        String fileId = CommonUtils.generateFileId();
        byte[] data = file.getBytes();
        String checksum = CommonUtils.calculateChecksum(data);
        
        FileMetadata metadata = new FileMetadata(
                fileId,
                file.getOriginalFilename(),
                data.length,
                checksum,
                Instant.now(),
                Instant.now(),
                "system", // Default owner
                replicationFactor
        );
        
        FileData fileData = new FileData(metadata, data);
        fileStorage.put(fileId, fileData);
        
        logger.info("Stored file: {} (size: {} bytes)", fileId, data.length);
        return fileId;
    }
    
    public FileData retrieveFile(String fileId) throws IOException {
        FileData fileData = fileStorage.get(fileId);
        if (fileData == null) {
            throw new IOException("File not found: " + fileId);
        }
        
        logger.info("Retrieved file: {} (size: {} bytes)", fileId, fileData.getData().length);
        return fileData;
    }
    
    public boolean deleteFile(String fileId) throws IOException {
        FileData removed = fileStorage.remove(fileId);
        boolean deleted = removed != null;
        
        if (deleted) {
            logger.info("Deleted file: {}", fileId);
        } else {
            logger.warn("File not found for deletion: {}", fileId);
        }
        
        return deleted;
    }
    
    public FileMetadata getFileMetadata(String fileId) throws IOException {
        FileData fileData = fileStorage.get(fileId);
        return fileData != null ? fileData.getMetadata() : null;
    }
    
    public List<String> listFiles() throws IOException {
        return new ArrayList<>(fileStorage.keySet());
    }
    
    public static class FileData {
        private final FileMetadata metadata;
        private final byte[] data;
        
        public FileData(FileMetadata metadata, byte[] data) {
            this.metadata = metadata;
            this.data = data.clone();
        }
        
        public FileMetadata getMetadata() {
            return metadata;
        }
        
        public byte[] getData() {
            return data.clone();
        }
    }
}