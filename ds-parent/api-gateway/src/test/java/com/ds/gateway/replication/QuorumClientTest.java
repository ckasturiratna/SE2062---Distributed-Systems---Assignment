package com.ds.gateway.replication;

import com.ds.common.replication.ChunkId;
import com.ds.common.replication.OrderedTimestamp;
import com.ds.common.replication.ReplicaMetadata;
import com.ds.common.replication.VersionVector;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuorumClientTest {

    static class StubQuorumClient extends QuorumClient {
        StubQuorumClient() { super(3, 2, 2, new ConflictResolver()); }
        @Override
        protected boolean sendPut(String nodeId, ChunkId chunkId, byte[] data) {
            return true;
        }
        @Override
        protected Replica sendGet(String nodeId, ChunkId chunkId) {
            VersionVector vv = new VersionVector();
            vv.set(nodeId, nodeId.equals("n3") ? 2 : 1);
            return new Replica(nodeId, new byte[]{}, new ReplicaMetadata(
                    chunkId, vv, new OrderedTimestamp(vv.get(nodeId), Instant.now(), nodeId), nodeId));
        }
    }

    @Test
    void writeMeetsQuorum() {
        QuorumClient qc = new StubQuorumClient();
        List<String> nodes = Arrays.asList("n1", "n2", "n3");
        qc.write(new ChunkId("f", 0), new byte[]{1,2,3}, nodes);
        assertTrue(qc.metric("quorum.write.success") >= 1);
    }

    @Test
    void readReturnsWinnerMetadata() {
        QuorumClient qc = new StubQuorumClient();
        List<String> nodes = Arrays.asList("n1", "n2", "n3");
        QuorumClient.ReadResult rr = qc.read(new ChunkId("f", 0), nodes);
        assertNotNull(rr);
        assertNotNull(rr.metadata);
    }
}
