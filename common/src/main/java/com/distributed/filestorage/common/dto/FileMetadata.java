package com.distributed.filestorage.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

public class FileMetadata {
    private final String fileId;
    private final String fileName;
    private final long size;
    private final String checksum;
    private final Instant createdAt;
    private final Instant lastModified;
    private final String owner;
    private final int replicationFactor;

    @JsonCreator
    public FileMetadata(
            @JsonProperty("fileId") String fileId,
            @JsonProperty("fileName") String fileName,
            @JsonProperty("size") long size,
            @JsonProperty("checksum") String checksum,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("lastModified") Instant lastModified,
            @JsonProperty("owner") String owner,
            @JsonProperty("replicationFactor") int replicationFactor) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.size = size;
        this.checksum = checksum;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
        this.owner = owner;
        this.replicationFactor = replicationFactor;
    }

    public String getFileId() { return fileId; }
    public String getFileName() { return fileName; }
    public long getSize() { return size; }
    public String getChecksum() { return checksum; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getLastModified() { return lastModified; }
    public String getOwner() { return owner; }
    public int getReplicationFactor() { return replicationFactor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMetadata that = (FileMetadata) o;
        return size == that.size &&
                replicationFactor == that.replicationFactor &&
                Objects.equals(fileId, that.fileId) &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(checksum, that.checksum) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(lastModified, that.lastModified) &&
                Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, fileName, size, checksum, createdAt, lastModified, owner, replicationFactor);
    }

    @Override
    public String toString() {
        return "FileMetadata{" +
                "fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", size=" + size +
                ", checksum='" + checksum + '\'' +
                ", createdAt=" + createdAt +
                ", lastModified=" + lastModified +
                ", owner='" + owner + '\'' +
                ", replicationFactor=" + replicationFactor +
                '}';
    }
}