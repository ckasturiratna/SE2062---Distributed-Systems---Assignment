package com.ds.gateway.replication;

import com.ds.common.replication.ChunkId;
import com.ds.common.replication.OrderedTimestamp;
import com.ds.common.replication.ReplicaMetadata;
import com.ds.common.replication.VersionVector;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConflictResolverTest {

    @Test
    void picksAfterOverBefore() {
        ConflictResolver r = new ConflictResolver();
        ChunkId cid = new ChunkId("f", 0);
        VersionVector a = new VersionVector();
        VersionVector b = new VersionVector();
        a.set("n1", 2);
        b.set("n1", 1);
        ReplicaMetadata ma = new ReplicaMetadata(cid, a, new OrderedTimestamp(2, Instant.now(), "n1"), "n1");
        ReplicaMetadata mb = new ReplicaMetadata(cid, b, new OrderedTimestamp(1, Instant.now(), "n1"), "n1");
        assertSame(ma, r.resolve(List.of(ma, mb)));
    }

    @Test
    void lwwOnConcurrent() {
        ConflictResolver r = new ConflictResolver();
        ChunkId cid = new ChunkId("f", 0);
        VersionVector a = new VersionVector();
        VersionVector b = new VersionVector();
        a.set("n1", 1);
        b.set("n2", 1);
        ReplicaMetadata ma = new ReplicaMetadata(cid, a, new OrderedTimestamp(1, Instant.ofEpochMilli(1000), "n1"), "n1");
        ReplicaMetadata mb = new ReplicaMetadata(cid, b, new OrderedTimestamp(1, Instant.ofEpochMilli(2000), "n2"), "n2");
        assertSame(mb, r.resolve(List.of(ma, mb)));
    }
}


