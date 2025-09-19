package com.distributed.filestorage.metadata.service;

import com.distributed.filestorage.common.dto.FileMetadata;
import com.distributed.filestorage.common.dto.FileLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.protocol.RaftClientReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Service for metadata operations using Raft consensus
 */
@Service
public class MetadataService {
    private static final Logger logger = LoggerFactory.getLogger(MetadataService.class);
    
    private final RaftClient raftClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    public MetadataService(RaftClient raftClient) {
        this.raftClient = raftClient;
    }
    
    public void storeMetadata(FileMetadata metadata) throws IOException, ExecutionException, InterruptedException {
        String payload = objectMapper.writeValueAsString(metadata);
        String command = "PUT_METADATA:" + payload;
        
        RaftClientReply reply = raftClient.io().send(Message.valueOf(command));
        if (!reply.isSuccess()) {
            throw new IOException("Failed to store metadata: " + reply.getException());
        }
        
        String result = reply.getMessage().getContent().toStringUtf8();
        if (!"SUCCESS".equals(result)) {
            throw new IOException("Metadata storage failed: " + result);
        }
        
        logger.info("Stored metadata for file: {}", metadata.getFileId());
    }
    
    public FileMetadata getMetadata(String fileId) throws IOException, ExecutionException, InterruptedException {
        String query = "GET_METADATA:" + fileId;
        
        RaftClientReply reply = raftClient.io().sendReadOnly(Message.valueOf(query));
        if (!reply.isSuccess()) {
            throw new IOException("Failed to get metadata: " + reply.getException());
        }
        
        String result = reply.getMessage().getContent().toStringUtf8();
        if ("NOT_FOUND".equals(result)) {
            return null;
        }
        
        if (result.startsWith("ERROR:")) {
            throw new IOException(result);
        }
        
        return objectMapper.readValue(result, FileMetadata.class);
    }
    
    public void deleteMetadata(String fileId) throws IOException, ExecutionException, InterruptedException {
        String command = "DELETE_METADATA:" + fileId;
        
        RaftClientReply reply = raftClient.io().send(Message.valueOf(command));
        if (!reply.isSuccess()) {
            throw new IOException("Failed to delete metadata: " + reply.getException());
        }
        
        String result = reply.getMessage().getContent().toStringUtf8();
        if (!"SUCCESS".equals(result) && !"NOT_FOUND".equals(result)) {
            throw new IOException("Metadata deletion failed: " + result);
        }
        
        logger.info("Deleted metadata for file: {}", fileId);
    }
    
    public void storeLocation(FileLocation location) throws IOException, ExecutionException, InterruptedException {
        String payload = objectMapper.writeValueAsString(location);
        String command = "PUT_LOCATION:" + payload;
        
        RaftClientReply reply = raftClient.io().send(Message.valueOf(command));
        if (!reply.isSuccess()) {
            throw new IOException("Failed to store location: " + reply.getException());
        }
        
        String result = reply.getMessage().getContent().toStringUtf8();
        if (!"SUCCESS".equals(result)) {
            throw new IOException("Location storage failed: " + result);
        }
        
        logger.info("Stored location for file: {}", location.getFileId());
    }
    
    public FileLocation getLocation(String fileId) throws IOException, ExecutionException, InterruptedException {
        String query = "GET_LOCATION:" + fileId;
        
        RaftClientReply reply = raftClient.io().sendReadOnly(Message.valueOf(query));
        if (!reply.isSuccess()) {
            throw new IOException("Failed to get location: " + reply.getException());
        }
        
        String result = reply.getMessage().getContent().toStringUtf8();
        if ("NOT_FOUND".equals(result)) {
            return null;
        }
        
        if (result.startsWith("ERROR:")) {
            throw new IOException(result);
        }
        
        return objectMapper.readValue(result, FileLocation.class);
    }
    
    public void deleteLocation(String fileId) throws IOException, ExecutionException, InterruptedException {
        String command = "DELETE_LOCATION:" + fileId;
        
        RaftClientReply reply = raftClient.io().send(Message.valueOf(command));
        if (!reply.isSuccess()) {
            throw new IOException("Failed to delete location: " + reply.getException());
        }
        
        String result = reply.getMessage().getContent().toStringUtf8();
        if (!"SUCCESS".equals(result) && !"NOT_FOUND".equals(result)) {
            throw new IOException("Location deletion failed: " + result);
        }
        
        logger.info("Deleted location for file: {}", fileId);
    }
    
    @SuppressWarnings("unchecked")
    public List<String> listFiles() throws IOException, ExecutionException, InterruptedException {
        String query = "LIST_FILES:";
        
        RaftClientReply reply = raftClient.io().sendReadOnly(Message.valueOf(query));
        if (!reply.isSuccess()) {
            throw new IOException("Failed to list files: " + reply.getException());
        }
        
        String result = reply.getMessage().getContent().toStringUtf8();
        if (result.startsWith("ERROR:")) {
            throw new IOException(result);
        }
        
        return objectMapper.readValue(result, List.class);
    }
}