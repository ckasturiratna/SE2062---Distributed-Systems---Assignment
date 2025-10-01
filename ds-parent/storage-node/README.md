Replication & Consistency (Storage Node)

Model
- N,R,W quorum with rule R + W > N for strong consistency.
- Each chunk is stored on N replicas across storage nodes.
- Version Vectors track causality; ties resolved via LWW using OrderedTimestamp (Lamport counter, wall clock, nodeId).

Operations
- PutChunk: writes data and bumps local vector clock; returns updated metadata (vector + timestamp).
- GetChunk: returns bytes and metadata.
- ReplicateChunk: scheduled by RepairManager when under-replicated.
- InventoryDigest: lists local replicas for reconciliation.

Consistency
- Strong consistency when R + W > N (e.g., N=3, W=2, R=2).
- Eventual consistency when quorums are small (e.g., N=3, W=1, R=1).

Read-Repair
- On reads, if divergence is detected, newest replica is selected; stale replicas are updated asynchronously.

Examples
- Strong: N=3, W=2, R=2 → reader intersects latest write set.
- Eventual: N=3, W=1, R=1 → updates propagate via repair.


