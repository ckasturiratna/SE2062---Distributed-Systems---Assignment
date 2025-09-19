package com.distributed.filestorage.client.service;

import com.distributed.filestorage.common.dto.FileMetadata;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * HTTP client for interacting with the file storage API
 */
public class ApiClient {
    private final String baseUrl;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }
    
    public String uploadFile(File file, int replicationFactor) throws IOException {
        String url = baseUrl + "/api/files/upload";
        
        HttpPost post = new HttpPost(url);
        
        FileBody fileBody = new FileBody(file);
        StringBody replicationBody = new StringBody(String.valueOf(replicationFactor), 
                org.apache.http.entity.ContentType.TEXT_PLAIN);
        
        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("file", fileBody)
                .addPart("replicationFactor", replicationBody)
                .build();
        
        post.setEntity(entity);
        
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Upload failed: " + response.getStatusLine().getReasonPhrase());
            }
            
            String responseBody = EntityUtils.toString(response.getEntity());
            Map<String, Object> result = objectMapper.readValue(responseBody, 
                    new TypeReference<Map<String, Object>>() {});
            
            return (String) result.get("fileId");
        }
    }
    
    public byte[] downloadFile(String fileId) throws IOException {
        String url = baseUrl + "/api/files/download/" + fileId;
        
        HttpGet get = new HttpGet(url);
        
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            if (response.getStatusLine().getStatusCode() == 404) {
                throw new IOException("File not found: " + fileId);
            } else if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Download failed: " + response.getStatusLine().getReasonPhrase());
            }
            
            return EntityUtils.toByteArray(response.getEntity());
        }
    }
    
    public boolean deleteFile(String fileId) throws IOException {
        String url = baseUrl + "/api/files/" + fileId;
        
        HttpDelete delete = new HttpDelete(url);
        
        try (CloseableHttpResponse response = httpClient.execute(delete)) {
            return response.getStatusLine().getStatusCode() == 200;
        }
    }
    
    public FileMetadata getFileMetadata(String fileId) throws IOException {
        String url = baseUrl + "/api/files/" + fileId + "/metadata";
        
        HttpGet get = new HttpGet(url);
        
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            if (response.getStatusLine().getStatusCode() == 404) {
                return null;
            } else if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Get metadata failed: " + response.getStatusLine().getReasonPhrase());
            }
            
            String responseBody = EntityUtils.toString(response.getEntity());
            return objectMapper.readValue(responseBody, FileMetadata.class);
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<String> listFiles() throws IOException {
        String url = baseUrl + "/api/files";
        
        HttpGet get = new HttpGet(url);
        
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("List files failed: " + response.getStatusLine().getReasonPhrase());
            }
            
            String responseBody = EntityUtils.toString(response.getEntity());
            return objectMapper.readValue(responseBody, List.class);
        }
    }
    
    public void close() throws IOException {
        httpClient.close();
    }
}