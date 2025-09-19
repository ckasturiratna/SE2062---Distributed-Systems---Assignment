package com.distributed.filestorage.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Utility class for common operations
 */
public class CommonUtils {
    
    /**
     * Generate a unique file ID
     */
    public static String generateFileId() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Generate a unique node ID
     */
    public static String generateNodeId() {
        return "node-" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * Calculate SHA-256 checksum of data
     */
    public static String calculateChecksum(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    /**
     * Validate file name
     */
    public static boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        
        // Check for invalid characters
        String invalidChars = "<>:\"|?*";
        for (char c : invalidChars.toCharArray()) {
            if (fileName.indexOf(c) >= 0) {
                return false;
            }
        }
        
        // Check for reserved names
        String[] reservedNames = {"CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "COM4", 
                                  "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", 
                                  "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};
        
        for (String reserved : reservedNames) {
            if (fileName.equalsIgnoreCase(reserved)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Format bytes to human readable format
     */
    public static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}