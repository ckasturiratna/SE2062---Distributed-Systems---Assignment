package com.distributed.filestorage.gateway.controller;

import com.distributed.filestorage.common.dto.FileMetadata;
import com.distributed.filestorage.gateway.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller for file operations
 */
@RestController
@RequestMapping("/api/files")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    
    private final FileService fileService;
    
    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    
    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "replicationFactor", defaultValue = "3") int replicationFactor) {
        
        try {
            logger.info("Uploading file: {} (size: {} bytes)", file.getOriginalFilename(), file.getSize());
            
            String fileId = fileService.storeFile(file, replicationFactor);
            
            FileUploadResponse response = new FileUploadResponse(fileId, file.getOriginalFilename(), 
                    file.getSize(), "File uploaded successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error uploading file: {}", file.getOriginalFilename(), e);
            FileUploadResponse response = new FileUploadResponse(null, file.getOriginalFilename(), 
                    file.getSize(), "Upload failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        try {
            logger.info("Downloading file: {}", fileId);
            
            FileService.FileData fileData = fileService.retrieveFile(fileId);
            
            ByteArrayResource resource = new ByteArrayResource(fileData.getData());
            
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(fileData.getData().length)
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + fileData.getMetadata().getFileName() + "\"")
                    .body(resource);
                    
        } catch (Exception e) {
            logger.error("Error downloading file: {}", fileId, e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{fileId}")
    public ResponseEntity<FileOperationResponse> deleteFile(@PathVariable String fileId) {
        try {
            logger.info("Deleting file: {}", fileId);
            
            boolean deleted = fileService.deleteFile(fileId);
            
            if (deleted) {
                return ResponseEntity.ok(new FileOperationResponse(fileId, "File deleted successfully"));
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("Error deleting file: {}", fileId, e);
            FileOperationResponse response = new FileOperationResponse(fileId, "Delete failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @GetMapping("/{fileId}/metadata")
    public ResponseEntity<FileMetadata> getFileMetadata(@PathVariable String fileId) {
        try {
            FileMetadata metadata = fileService.getFileMetadata(fileId);
            
            if (metadata != null) {
                return ResponseEntity.ok(metadata);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("Error getting file metadata: {}", fileId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<String>> listFiles() {
        try {
            List<String> files = fileService.listFiles();
            return ResponseEntity.ok(files);
            
        } catch (Exception e) {
            logger.error("Error listing files", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    public static class FileUploadResponse {
        private final String fileId;
        private final String fileName;
        private final long size;
        private final String message;
        
        public FileUploadResponse(String fileId, String fileName, long size, String message) {
            this.fileId = fileId;
            this.fileName = fileName;
            this.size = size;
            this.message = message;
        }
        
        public String getFileId() { return fileId; }
        public String getFileName() { return fileName; }
        public long getSize() { return size; }
        public String getMessage() { return message; }
    }
    
    public static class FileOperationResponse {
        private final String fileId;
        private final String message;
        
        public FileOperationResponse(String fileId, String message) {
            this.fileId = fileId;
            this.message = message;
        }
        
        public String getFileId() { return fileId; }
        public String getMessage() { return message; }
    }
}