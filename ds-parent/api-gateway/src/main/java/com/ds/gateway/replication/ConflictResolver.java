package com.ds.gateway.replication;

import com.ds.common.replication.OrderedTimestamp;
import com.ds.common.replication.ReplicaMetadata;
import com.ds.common.replication.VersionVector;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Resolves conflicts across replicas using Version Vectors and LWW tie-break.
 */
public final class ConflictResolver {

    public ReplicaMetadata resolve(List<ReplicaMetadata> replicas) {
        Objects.requireNonNull(replicas);
        if (replicas.isEmpty()) throw new IllegalArgumentException("replicas must be non-empty");

        ReplicaMetadata winner = replicas.get(0);
        for (int i = 1; i < replicas.size(); i++) {
            winner = pickNewer(winner, replicas.get(i));
        }
        return winner;
    }

    private ReplicaMetadata pickNewer(ReplicaMetadata a, ReplicaMetadata b) {
        VersionVector.Relation rel = a.versionVector().compare(b.versionVector());
        if (rel == VersionVector.Relation.AFTER) {
            return a;
        } else if (rel == VersionVector.Relation.BEFORE) {
            return b;
        } else {
            return lww(a, b);
        }
    }

    private static ReplicaMetadata lww(ReplicaMetadata a, ReplicaMetadata b) {
        OrderedTimestamp ta = a.timestamp();
        OrderedTimestamp tb = b.timestamp();
        int cmp = ta.compareTo(tb);
        if (cmp >= 0) return a; // prefer a when equal
        return b;
    }
}


