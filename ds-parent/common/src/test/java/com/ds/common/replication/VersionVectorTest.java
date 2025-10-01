package com.ds.common.replication;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VersionVectorTest {

    @Test
    void incrementAndCompare() {
        VersionVector a = new VersionVector();
        VersionVector b = new VersionVector();
        a.increment("n1");
        assertEquals(VersionVector.Relation.AFTER, a.compare(b));
        assertEquals(VersionVector.Relation.BEFORE, b.compare(a));
    }

    @Test
    void mergeKeepsMax() {
        VersionVector a = new VersionVector();
        VersionVector b = new VersionVector();
        a.set("n1", 2);
        b.set("n1", 5);
        a.merge(b);
        assertEquals(VersionVector.Relation.EQUAL, a.compare(b));
        assertEquals(5, a.get("n1"));
    }

    @Test
    void concurrentWhenMixed() {
        VersionVector a = new VersionVector();
        VersionVector b = new VersionVector();
        a.set("n1", 2);
        b.set("n1", 1);
        b.set("n2", 1);
        assertEquals(VersionVector.Relation.CONCURRENT, a.compare(b));
    }
}


