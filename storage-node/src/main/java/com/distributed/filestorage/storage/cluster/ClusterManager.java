package com.distributed.filestorage.storage.cluster;

import com.distributed.filestorage.common.dto.NodeInfo;
import org.jgroups.*;
import org.jgroups.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * JGroups-based cluster manager for storage nodes
 */
@Component
public class ClusterManager implements Receiver {
    private static final Logger logger = LoggerFactory.getLogger(ClusterManager.class);
    
    private JChannel channel;
    private final List<Address> members = new CopyOnWriteArrayList<>();
    private final List<ClusterEventListener> listeners = new CopyOnWriteArrayList<>();
    
    @Value("${storage.node.id}")
    private String nodeId;
    
    @Value("${storage.cluster.name:storage-cluster}")
    private String clusterName;
    
    @PostConstruct
    public void start() throws Exception {
        logger.info("Starting cluster manager for node: {}", nodeId);
        
        // Use default UDP configuration
        channel = new JChannel();
        
        channel.setName(nodeId);
        channel.setReceiver(this);
        channel.connect(clusterName);
        
        logger.info("Connected to cluster: {} as node: {}", clusterName, nodeId);
    }
    
    @PreDestroy
    public void stop() {
        if (channel != null) {
            logger.info("Disconnecting from cluster");
            channel.close();
        }
    }
    
    @Override
    public void viewAccepted(View view) {
        logger.info("Cluster view changed: {}", view);
        
        List<Address> oldMembers = List.copyOf(members);
        members.clear();
        members.addAll(view.getMembers());
        
        // Notify listeners of membership changes
        for (ClusterEventListener listener : listeners) {
            listener.onMembershipChanged(oldMembers, members);
        }
    }
    
    @Override
    public void receive(Message msg) {
        try {
            String content = new String(msg.getArray(), msg.getOffset(), msg.getLength());
            logger.debug("Received message from {}: {}", msg.getSrc(), content);
            
            // Parse and handle cluster messages
            handleClusterMessage(msg.getSrc(), content);
        } catch (Exception e) {
            logger.error("Error processing cluster message", e);
        }
    }
    
    private void handleClusterMessage(Address sender, String content) {
        // Handle different types of cluster messages
        String[] parts = content.split(":", 2);
        if (parts.length != 2) {
            logger.warn("Invalid message format: {}", content);
            return;
        }
        
        String messageType = parts[0];
        String payload = parts[1];
        
        switch (messageType) {
            case "HEARTBEAT":
                handleHeartbeat(sender, payload);
                break;
            case "REPLICATION_REQUEST":
                handleReplicationRequest(sender, payload);
                break;
            case "NODE_STATUS":
                handleNodeStatus(sender, payload);
                break;
            default:
                logger.warn("Unknown message type: {}", messageType);
        }
    }
    
    private void handleHeartbeat(Address sender, String payload) {
        logger.debug("Received heartbeat from: {}", sender);
        // Update node status and last seen time
        for (ClusterEventListener listener : listeners) {
            listener.onHeartbeat(sender, payload);
        }
    }
    
    private void handleReplicationRequest(Address sender, String payload) {
        logger.debug("Received replication request from: {}", sender);
        for (ClusterEventListener listener : listeners) {
            listener.onReplicationRequest(sender, payload);
        }
    }
    
    private void handleNodeStatus(Address sender, String payload) {
        logger.debug("Received node status from: {}", sender);
        for (ClusterEventListener listener : listeners) {
            listener.onNodeStatusUpdate(sender, payload);
        }
    }
    
    public void sendMessage(String messageType, String payload) {
        try {
            String message = messageType + ":" + payload;
            Message msg = new EmptyMessage(null).setArray(message.getBytes());
            channel.send(msg);
            logger.debug("Sent message: {}", message);
        } catch (Exception e) {
            logger.error("Error sending cluster message", e);
        }
    }
    
    public void sendHeartbeat() {
        sendMessage("HEARTBEAT", nodeId);
    }
    
    public void sendReplicationRequest(String fileId, int chunkIndex, String targetNode) {
        String payload = String.format("%s,%d,%s", fileId, chunkIndex, targetNode);
        sendMessage("REPLICATION_REQUEST", payload);
    }
    
    public void addListener(ClusterEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(ClusterEventListener listener) {
        listeners.remove(listener);
    }
    
    public List<Address> getMembers() {
        return List.copyOf(members);
    }
    
    public String getNodeId() {
        return nodeId;
    }
    
    public Address getLocalAddress() {
        return channel != null ? channel.getAddress() : null;
    }
    
    public interface ClusterEventListener {
        void onMembershipChanged(List<Address> oldMembers, List<Address> newMembers);
        void onHeartbeat(Address sender, String payload);
        void onReplicationRequest(Address sender, String payload);
        void onNodeStatusUpdate(Address sender, String payload);
    }
}