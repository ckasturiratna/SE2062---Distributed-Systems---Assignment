package com.distributed.filestorage.test.scenarios;

import com.distributed.filestorage.client.service.ApiClient;
import com.distributed.filestorage.common.dto.FileMetadata;
import com.distributed.filestorage.common.utils.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Integration tests for the distributed file storage system
 */
public class IntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(IntegrationTest.class);
    
    private ApiClient apiClient;
    private Path tempDir;
    
    @BeforeEach
    public void setUp() throws IOException {
        // Configure API client to connect to test environment
        String gatewayUrl = System.getProperty("gateway.url", "http://localhost:8080");
        apiClient = new ApiClient(gatewayUrl);
        
        // Create temporary directory for test files
        tempDir = Files.createTempDirectory("filestorage-test");
        logger.info("Test setup complete. Gateway URL: {}, Temp dir: {}", gatewayUrl, tempDir);
    }
    
    @Test
    public void testBasicFileOperations() throws Exception {
        logger.info("Starting basic file operations test");
        
        // Create a test file
        Path testFile = createTestFile("test-basic.txt", "Hello, World!".getBytes());
        
        // Upload the file
        String fileId = apiClient.uploadFile(testFile.toFile(), 2);
        logger.info("Uploaded file with ID: {}", fileId);
        
        // Verify metadata
        FileMetadata metadata = apiClient.getFileMetadata(fileId);
        assert metadata != null;
        assert metadata.getFileName().equals("test-basic.txt");
        assert metadata.getSize() == "Hello, World!".getBytes().length;
        
        // Download the file
        byte[] downloadedData = apiClient.downloadFile(fileId);
        assert new String(downloadedData).equals("Hello, World!");
        
        // List files
        List<String> files = apiClient.listFiles();
        assert files.contains(fileId);
        
        // Delete the file
        boolean deleted = apiClient.deleteFile(fileId);
        assert deleted;
        
        // Verify deletion
        files = apiClient.listFiles();
        assert !files.contains(fileId);
        
        logger.info("Basic file operations test completed successfully");
    }
    
    private Path createTestFile(String fileName, byte[] content) throws IOException {
        Path filePath = tempDir.resolve(fileName);
        Files.write(filePath, content);
        return filePath;
    }
}