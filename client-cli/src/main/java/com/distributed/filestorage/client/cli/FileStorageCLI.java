package com.distributed.filestorage.client.cli;

import com.distributed.filestorage.client.service.ApiClient;
import com.distributed.filestorage.common.dto.FileMetadata;
import com.distributed.filestorage.common.utils.CommonUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Command Line Interface for the distributed file storage system
 */
@Command(name = "filestorage", mixinStandardHelpOptions = true, version = "1.0",
         description = "Distributed File Storage System CLI")
public class FileStorageCLI implements Callable<Integer> {
    
    @Option(names = {"-s", "--server"}, description = "API Gateway server URL", 
            defaultValue = "http://localhost:8080")
    private String serverUrl;
    
    @Option(names = {"-v", "--verbose"}, description = "Enable verbose output")
    private boolean verbose;
    
    public static void main(String[] args) {
        int exitCode = new CommandLine(new FileStorageCLI()).execute(args);
        System.exit(exitCode);
    }
    
    @Override
    public Integer call() {
        System.out.println("Distributed File Storage System CLI");
        System.out.println("Use --help to see available commands");
        return 0;
    }
    
    @Command(name = "upload", description = "Upload a file to the storage system")
    public static class UploadCommand implements Callable<Integer> {
        
        @CommandLine.ParentCommand
        private FileStorageCLI parent;
        
        @Parameters(index = "0", description = "Path to the file to upload")
        private File file;
        
        @Option(names = {"-r", "--replication"}, description = "Replication factor", defaultValue = "3")
        private int replicationFactor;
        
        @Override
        public Integer call() throws Exception {
            if (!file.exists() || !file.isFile()) {
                System.err.println("File does not exist or is not a regular file: " + file.getPath());
                return 1;
            }
            
            ApiClient client = new ApiClient(parent.serverUrl);
            
            try {
                if (parent.verbose) {
                    System.out.println("Uploading file: " + file.getName() + " (" + 
                            CommonUtils.formatBytes(file.length()) + ")");
                }
                
                String fileId = client.uploadFile(file, replicationFactor);
                
                System.out.println("File uploaded successfully");
                System.out.println("File ID: " + fileId);
                
                return 0;
                
            } catch (IOException e) {
                System.err.println("Upload failed: " + e.getMessage());
                if (parent.verbose) {
                    e.printStackTrace();
                }
                return 1;
            }
        }
    }
    
    @Command(name = "download", description = "Download a file from the storage system")
    public static class DownloadCommand implements Callable<Integer> {
        
        @CommandLine.ParentCommand
        private FileStorageCLI parent;
        
        @Parameters(index = "0", description = "File ID to download")
        private String fileId;
        
        @Option(names = {"-o", "--output"}, description = "Output file path")
        private String outputPath;
        
        @Override
        public Integer call() throws Exception {
            ApiClient client = new ApiClient(parent.serverUrl);
            
            try {
                if (parent.verbose) {
                    System.out.println("Downloading file: " + fileId);
                }
                
                byte[] data = client.downloadFile(fileId);
                
                // Determine output path
                String finalOutputPath;
                if (outputPath != null) {
                    finalOutputPath = outputPath;
                } else {
                    // Try to get original filename from metadata
                    try {
                        FileMetadata metadata = client.getFileMetadata(fileId);
                        finalOutputPath = metadata != null ? metadata.getFileName() : fileId;
                    } catch (Exception e) {
                        finalOutputPath = fileId;
                    }
                }
                
                Files.write(Paths.get(finalOutputPath), data);
                
                System.out.println("File downloaded successfully");
                System.out.println("Saved to: " + finalOutputPath);
                System.out.println("Size: " + CommonUtils.formatBytes(data.length));
                
                return 0;
                
            } catch (IOException e) {
                System.err.println("Download failed: " + e.getMessage());
                if (parent.verbose) {
                    e.printStackTrace();
                }
                return 1;
            }
        }
    }
    
    @Command(name = "delete", description = "Delete a file from the storage system")
    public static class DeleteCommand implements Callable<Integer> {
        
        @CommandLine.ParentCommand
        private FileStorageCLI parent;
        
        @Parameters(index = "0", description = "File ID to delete")
        private String fileId;
        
        @Override
        public Integer call() throws Exception {
            ApiClient client = new ApiClient(parent.serverUrl);
            
            try {
                if (parent.verbose) {
                    System.out.println("Deleting file: " + fileId);
                }
                
                boolean deleted = client.deleteFile(fileId);
                
                if (deleted) {
                    System.out.println("File deleted successfully");
                    return 0;
                } else {
                    System.err.println("File not found: " + fileId);
                    return 1;
                }
                
            } catch (IOException e) {
                System.err.println("Delete failed: " + e.getMessage());
                if (parent.verbose) {
                    e.printStackTrace();
                }
                return 1;
            }
        }
    }
    
    @Command(name = "list", description = "List all files in the storage system")
    public static class ListCommand implements Callable<Integer> {
        
        @CommandLine.ParentCommand
        private FileStorageCLI parent;
        
        @Option(names = {"-d", "--details"}, description = "Show detailed information")
        private boolean details;
        
        @Override
        public Integer call() throws Exception {
            ApiClient client = new ApiClient(parent.serverUrl);
            
            try {
                List<String> files = client.listFiles();
                
                if (files.isEmpty()) {
                    System.out.println("No files found");
                    return 0;
                }
                
                System.out.println("Files (" + files.size() + "):");
                
                for (String fileId : files) {
                    if (details) {
                        try {
                            FileMetadata metadata = client.getFileMetadata(fileId);
                            if (metadata != null) {
                                System.out.printf("  %s - %s (%s) [%s]%n",
                                        fileId,
                                        metadata.getFileName(),
                                        CommonUtils.formatBytes(metadata.getSize()),
                                        metadata.getCreatedAt());
                            } else {
                                System.out.println("  " + fileId + " (metadata not available)");
                            }
                        } catch (Exception e) {
                            System.out.println("  " + fileId + " (error getting metadata)");
                        }
                    } else {
                        System.out.println("  " + fileId);
                    }
                }
                
                return 0;
                
            } catch (IOException e) {
                System.err.println("List failed: " + e.getMessage());
                if (parent.verbose) {
                    e.printStackTrace();
                }
                return 1;
            }
        }
    }
    
    @Command(name = "info", description = "Get file information")
    public static class InfoCommand implements Callable<Integer> {
        
        @CommandLine.ParentCommand
        private FileStorageCLI parent;
        
        @Parameters(index = "0", description = "File ID")
        private String fileId;
        
        @Override
        public Integer call() throws Exception {
            ApiClient client = new ApiClient(parent.serverUrl);
            
            try {
                FileMetadata metadata = client.getFileMetadata(fileId);
                
                if (metadata == null) {
                    System.err.println("File not found: " + fileId);
                    return 1;
                }
                
                System.out.println("File Information:");
                System.out.println("  File ID: " + metadata.getFileId());
                System.out.println("  File Name: " + metadata.getFileName());
                System.out.println("  Size: " + CommonUtils.formatBytes(metadata.getSize()));
                System.out.println("  Checksum: " + metadata.getChecksum());
                System.out.println("  Created: " + metadata.getCreatedAt());
                System.out.println("  Modified: " + metadata.getLastModified());
                System.out.println("  Owner: " + metadata.getOwner());
                System.out.println("  Replication Factor: " + metadata.getReplicationFactor());
                
                return 0;
                
            } catch (IOException e) {
                System.err.println("Info failed: " + e.getMessage());
                if (parent.verbose) {
                    e.printStackTrace();
                }
                return 1;
            }
        }
    }
}