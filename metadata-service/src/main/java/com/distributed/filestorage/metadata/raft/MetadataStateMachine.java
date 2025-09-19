package com.distributed.filestorage.metadata.raft;

import com.distributed.filestorage.common.dto.FileMetadata;
import com.distributed.filestorage.common.dto.FileLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Raft state machine for metadata operations
 */
public class MetadataStateMachine extends BaseStateMachine {
    private static final Logger logger = LoggerFactory.getLogger(MetadataStateMachine.class);
    
    private final Map<String, FileMetadata> fileMetadata = new ConcurrentHashMap<>();
    private final Map<String, FileLocation> fileLocations = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public CompletableFuture<Message> applyTransaction(TransactionContext trx) {
        final RaftProtos.LogEntryProto entry = trx.getLogEntry();
        final String command = entry.getStateMachineLogEntry().getLogData().toStringUtf8();
        
        logger.debug("Applying transaction: {}", command);
        
        try {
            String result = processCommand(command);
            return CompletableFuture.completedFuture(Message.valueOf(result));
        } catch (Exception e) {
            logger.error("Error processing command: {}", command, e);
            return CompletableFuture.completedFuture(Message.valueOf("ERROR: " + e.getMessage()));
        }
    }
    
    private String processCommand(String command) throws JsonProcessingException {
        String[] parts = command.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid command format");
        }
        
        String operation = parts[0];
        String payload = parts[1];
        
        switch (operation) {
            case "PUT_METADATA":
                return putMetadata(payload);
            case "GET_METADATA":
                return getMetadata(payload);
            case "DELETE_METADATA":
                return deleteMetadata(payload);
            case "PUT_LOCATION":
                return putLocation(payload);
            case "GET_LOCATION":
                return getLocation(payload);
            case "DELETE_LOCATION":
                return deleteLocation(payload);
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }
    
    private String putMetadata(String payload) throws JsonProcessingException {
        FileMetadata metadata = objectMapper.readValue(payload, FileMetadata.class);
        fileMetadata.put(metadata.getFileId(), metadata);
        return "SUCCESS";
    }
    
    private String getMetadata(String fileId) throws JsonProcessingException {
        FileMetadata metadata = fileMetadata.get(fileId);
        if (metadata == null) {
            return "NOT_FOUND";
        }
        return objectMapper.writeValueAsString(metadata);
    }
    
    private String deleteMetadata(String fileId) {
        FileMetadata removed = fileMetadata.remove(fileId);
        return removed != null ? "SUCCESS" : "NOT_FOUND";
    }
    
    private String putLocation(String payload) throws JsonProcessingException {
        FileLocation location = objectMapper.readValue(payload, FileLocation.class);
        fileLocations.put(location.getFileId(), location);
        return "SUCCESS";
    }
    
    private String getLocation(String fileId) throws JsonProcessingException {
        FileLocation location = fileLocations.get(fileId);
        if (location == null) {
            return "NOT_FOUND";
        }
        return objectMapper.writeValueAsString(location);
    }
    
    private String deleteLocation(String fileId) {
        FileLocation removed = fileLocations.remove(fileId);
        return removed != null ? "SUCCESS" : "NOT_FOUND";
    }
    
    @Override
    public CompletableFuture<Message> query(Message request) {
        final String query = request.getContent().toStringUtf8();
        
        try {
            String result = processQuery(query);
            return CompletableFuture.completedFuture(Message.valueOf(result));
        } catch (Exception e) {
            logger.error("Error processing query: {}", query, e);
            return CompletableFuture.completedFuture(Message.valueOf("ERROR: " + e.getMessage()));
        }
    }
    
    private String processQuery(String query) throws JsonProcessingException {
        String[] parts = query.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid query format");
        }
        
        String operation = parts[0];
        String payload = parts[1];
        
        switch (operation) {
            case "GET_METADATA":
                return getMetadata(payload);
            case "GET_LOCATION":
                return getLocation(payload);
            case "LIST_FILES":
                return listFiles();
            default:
                throw new IllegalArgumentException("Unknown query operation: " + operation);
        }
    }
    
    private String listFiles() throws JsonProcessingException {
        return objectMapper.writeValueAsString(fileMetadata.keySet());
    }
}