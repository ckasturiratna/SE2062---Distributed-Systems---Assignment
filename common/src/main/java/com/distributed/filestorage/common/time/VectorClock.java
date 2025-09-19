package com.distributed.filestorage.common.time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Vector clock implementation for distributed systems.
 * Provides partial ordering of events across nodes.
 */
public class VectorClock {
    private static final Logger logger = LoggerFactory.getLogger(VectorClock.class);
    
    private final AtomicLong[] clocks;
    private final String[] nodeIds;
    private final int nodeIndex;
    
    public VectorClock(String[] nodeIds, String currentNodeId) {
        this.nodeIds = nodeIds.clone();
        this.clocks = new AtomicLong[nodeIds.length];
        
        int index = -1;
        for (int i = 0; i < nodeIds.length; i++) {
            this.clocks[i] = new AtomicLong(0);
            if (nodeIds[i].equals(currentNodeId)) {
                index = i;
            }
        }
        
        if (index == -1) {
            throw new IllegalArgumentException("Current node ID not found in node list");
        }
        
        this.nodeIndex = index;
    }
    
    /**
     * Increment local clock and return new timestamp
     */
    public long[] tick() {
        clocks[nodeIndex].incrementAndGet();
        return getCurrentTimestamp();
    }
    
    /**
     * Update vector clock with received timestamp
     */
    public long[] update(long[] receivedTimestamp) {
        if (receivedTimestamp.length != clocks.length) {
            throw new IllegalArgumentException("Timestamp vector length mismatch");
        }
        
        for (int i = 0; i < clocks.length; i++) {
            if (i == nodeIndex) {
                clocks[i].incrementAndGet();
            } else {
                long current = clocks[i].get();
                long received = receivedTimestamp[i];
                clocks[i].set(Math.max(current, received));
            }
        }
        
        return getCurrentTimestamp();
    }
    
    /**
     * Get current vector timestamp
     */
    public long[] getCurrentTimestamp() {
        long[] result = new long[clocks.length];
        for (int i = 0; i < clocks.length; i++) {
            result[i] = clocks[i].get();
        }
        return result;
    }
    
    /**
     * Compare this vector clock with another
     * Returns:
     * -1 if this < other (this happened before other)
     * 0 if this and other are concurrent
     * 1 if this > other (this happened after other)
     */
    public int compareTo(long[] other) {
        if (other.length != clocks.length) {
            throw new IllegalArgumentException("Timestamp vector length mismatch");
        }
        
        boolean thisSmaller = false;
        boolean thisLarger = false;
        
        for (int i = 0; i < clocks.length; i++) {
            long thisClock = clocks[i].get();
            long otherClock = other[i];
            
            if (thisClock < otherClock) {
                thisSmaller = true;
            } else if (thisClock > otherClock) {
                thisLarger = true;
            }
        }
        
        if (thisSmaller && !thisLarger) {
            return -1; // this < other
        } else if (thisLarger && !thisSmaller) {
            return 1;  // this > other
        } else {
            return 0;  // concurrent
        }
    }
    
    public String[] getNodeIds() {
        return nodeIds.clone();
    }
    
    public int getNodeIndex() {
        return nodeIndex;
    }
}