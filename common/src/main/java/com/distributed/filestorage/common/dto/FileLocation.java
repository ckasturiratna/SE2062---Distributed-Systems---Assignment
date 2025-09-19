package com.distributed.filestorage.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class FileLocation {
    private final String fileId;
    private final List<String> nodeIds;
    private final String primaryNodeId;

    @JsonCreator
    public FileLocation(
            @JsonProperty("fileId") String fileId,
            @JsonProperty("nodeIds") List<String> nodeIds,
            @JsonProperty("primaryNodeId") String primaryNodeId) {
        this.fileId = fileId;
        this.nodeIds = nodeIds;
        this.primaryNodeId = primaryNodeId;
    }

    public String getFileId() { return fileId; }
    public List<String> getNodeIds() { return nodeIds; }
    public String getPrimaryNodeId() { return primaryNodeId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileLocation that = (FileLocation) o;
        return Objects.equals(fileId, that.fileId) &&
                Objects.equals(nodeIds, that.nodeIds) &&
                Objects.equals(primaryNodeId, that.primaryNodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, nodeIds, primaryNodeId);
    }

    @Override
    public String toString() {
        return "FileLocation{" +
                "fileId='" + fileId + '\'' +
                ", nodeIds=" + nodeIds +
                ", primaryNodeId='" + primaryNodeId + '\'' +
                '}';
    }
}