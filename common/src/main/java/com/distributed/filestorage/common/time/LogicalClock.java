package com.distributed.filestorage.common.time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Logical clock implementation for distributed systems.
 * Provides Lamport timestamps for ordering events across nodes.
 */
public class LogicalClock {
    private static final Logger logger = LoggerFactory.getLogger(LogicalClock.class);
    
    private final AtomicLong counter = new AtomicLong(0);
    private final String nodeId;
    private final ScheduledExecutorService scheduler;
    
    public LogicalClock(String nodeId) {
        this.nodeId = nodeId;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "LogicalClock-" + nodeId);
            t.setDaemon(true);
            return t;
        });
        
        // Periodic tick to advance clock
        scheduler.scheduleAtFixedRate(this::tick, 1, 1, TimeUnit.SECONDS);
    }
    
    /**
     * Get current logical timestamp
     */
    public long now() {
        return counter.incrementAndGet();
    }
    
    /**
     * Update clock with received timestamp (for Lamport clock algorithm)
     */
    public long update(long receivedTimestamp) {
        long current = counter.get();
        long newTimestamp = Math.max(current, receivedTimestamp) + 1;
        counter.set(newTimestamp);
        return newTimestamp;
    }
    
    /**
     * Periodic tick to advance clock
     */
    private void tick() {
        counter.incrementAndGet();
    }
    
    /**
     * Shutdown the clock
     */
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    public String getNodeId() {
        return nodeId;
    }
}