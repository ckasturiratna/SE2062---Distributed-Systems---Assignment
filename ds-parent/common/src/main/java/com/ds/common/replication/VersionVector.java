package com.ds.common.replication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Version vector (vector clock) used for detecting causality and concurrency across replicas.
 */
public final class VersionVector {

    public enum Relation { BEFORE, AFTER, CONCURRENT, EQUAL }

    private final Map<String, Long> counters;

    public VersionVector() {
        this.counters = new HashMap<>();
    }

    public VersionVector(Map<String, Long> initial) {
        this.counters = new HashMap<>(Objects.requireNonNull(initial));
    }

    public synchronized void increment(String nodeId) {
        counters.merge(nodeId, 1L, Long::sum);
    }

    public synchronized void set(String nodeId, long value) {
        counters.put(nodeId, value);
    }

    public synchronized long get(String nodeId) {
        return counters.getOrDefault(nodeId, 0L);
    }

    public synchronized Map<String, Long> snapshot() {
        return Collections.unmodifiableMap(new HashMap<>(counters));
    }

    public synchronized void merge(VersionVector other) {
        other.counters.forEach((node, val) ->
            counters.merge(node, val, Math::max)
        );
    }

    public synchronized Relation compare(VersionVector other) {
        boolean thisStrictlyGreater = false;
        boolean otherStrictlyGreater = false;

        for (String node : unionKeys(this.counters, other.counters)) {
            long a = this.counters.getOrDefault(node, 0L);
            long b = other.counters.getOrDefault(node, 0L);
            if (a < b) otherStrictlyGreater = true;
            if (a > b) thisStrictlyGreater = true;
            if (thisStrictlyGreater && otherStrictlyGreater) {
                return Relation.CONCURRENT;
            }
        }
        if (thisStrictlyGreater) return Relation.AFTER;
        if (otherStrictlyGreater) return Relation.BEFORE;
        return Relation.EQUAL;
    }

    private static Iterable<String> unionKeys(Map<String, Long> a, Map<String, Long> b) {
        Map<String, Long> u = new HashMap<>(a);
        u.putAll(b);
        return u.keySet();
    }

    @Override
    public synchronized String toString() {
        return counters.toString();
    }
}


