package com.distributed.filestorage.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class NodeInfo {
    private final String nodeId;
    private final String host;
    private final int port;
    private final NodeStatus status;
    private final long lastHeartbeat;
    private final double loadFactor;

    @JsonCreator
    public NodeInfo(
            @JsonProperty("nodeId") String nodeId,
            @JsonProperty("host") String host,
            @JsonProperty("port") int port,
            @JsonProperty("status") NodeStatus status,
            @JsonProperty("lastHeartbeat") long lastHeartbeat,
            @JsonProperty("loadFactor") double loadFactor) {
        this.nodeId = nodeId;
        this.host = host;
        this.port = port;
        this.status = status;
        this.lastHeartbeat = lastHeartbeat;
        this.loadFactor = loadFactor;
    }

    public String getNodeId() { return nodeId; }
    public String getHost() { return host; }
    public int getPort() { return port; }
    public NodeStatus getStatus() { return status; }
    public long getLastHeartbeat() { return lastHeartbeat; }
    public double getLoadFactor() { return loadFactor; }

    public String getAddress() {
        return host + ":" + port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeInfo nodeInfo = (NodeInfo) o;
        return port == nodeInfo.port &&
                lastHeartbeat == nodeInfo.lastHeartbeat &&
                Double.compare(nodeInfo.loadFactor, loadFactor) == 0 &&
                Objects.equals(nodeId, nodeInfo.nodeId) &&
                Objects.equals(host, nodeInfo.host) &&
                status == nodeInfo.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, host, port, status, lastHeartbeat, loadFactor);
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "nodeId='" + nodeId + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", status=" + status +
                ", lastHeartbeat=" + lastHeartbeat +
                ", loadFactor=" + loadFactor +
                '}';
    }

    public enum NodeStatus {
        ACTIVE,
        INACTIVE,
        FAILED,
        STARTING,
        STOPPING
    }
}