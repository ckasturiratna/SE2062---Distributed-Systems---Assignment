package com.example.ds.storage;

import com.example.ds.common.metadata.MetadataClient;
import com.example.ds.storage.membership.MembershipManager;
import com.example.ds.storage.repair.RepairController;
import com.example.ds.storage.repair.StubReplicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        String nodeId = System.getProperty("nodeId", "node-" + (int)(Math.random()*1000));
        String cluster = System.getProperty("cluster", "ds-cluster");

        // Provide a simple in-memory metadata client for testing
        MetadataClient metadataClient = InMemoryMetadataClient.defaultDemo();

        try (MembershipManager membership = new MembershipManager(cluster, nodeId)) {
            membership.start();

            try (RepairController controller = new RepairController(metadataClient, membership, new StubReplicator())) {
                log.info("Storage node {} up. Press Ctrl+C to exit.", nodeId);
                Thread.currentThread().join();
            }
        }
    }
}


