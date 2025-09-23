package com.ds.common.time;

/**
 * Marker interface for entities that carry an {@link OrderedTimestamp}.
 */
public interface Clocked {

    /**
     * Returns the associated timestamp.
     */
    OrderedTimestamp getTs();
}
