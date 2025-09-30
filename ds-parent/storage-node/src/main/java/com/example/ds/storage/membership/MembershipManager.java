package com.example.ds.storage.membership;

import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

public class MembershipManager extends ReceiverAdapter implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(MembershipManager.class);

    private final String clusterName;
    private final String nodeId;
    private final JChannel channel;
    private final Set<String> liveNodes = Collections.synchronizedSet(new HashSet<>());
    private final CopyOnWriteArrayList<BiConsumer<String, Boolean>> listeners = new CopyOnWriteArrayList<>();

    public MembershipManager(String clusterName, String nodeId) throws Exception {
        this.clusterName = clusterName;
        this.nodeId = nodeId;
        this.channel = new JChannel();
        this.channel.setReceiver(this);
    }

    public void start() throws Exception {
        channel.connect(clusterName);
        log.info("NodeUp: {} joined cluster {}", nodeId, clusterName);
    }

    public void addListener(BiConsumer<String, Boolean> listener) {
        listeners.add(listener);
    }

    public Set<String> getLiveNodesSnapshot() {
        synchronized (liveNodes) {
            return new HashSet<>(liveNodes);
        }
    }

    @Override
    public void viewAccepted(View newView) {
        Set<String> members = new HashSet<>();
        newView.getMembers().forEach(a -> members.add(a.toString()));

        Set<String> previous;
        synchronized (liveNodes) {
            previous = new HashSet<>(liveNodes);
            liveNodes.clear();
            liveNodes.addAll(members);
        }

        // Detect joins
        for (String m : members) {
            if (!previous.contains(m)) {
                log.info("NodeUp: {}", m);
                notifyListeners(m, true);
            }
        }
        // Detect leaves
        for (String p : previous) {
            if (!members.contains(p)) {
                log.warn("NodeDown: {}", p);
                notifyListeners(p, false);
            }
        }
    }

    private void notifyListeners(String affectedNodeId, boolean isUp) {
        for (BiConsumer<String, Boolean> l : listeners) {
            try {
                l.accept(affectedNodeId, isUp);
            } catch (Exception e) {
                log.error("Listener error", e);
            }
        }
    }

    @Override
    public void close() throws Exception {
        try {
            channel.close();
        } finally {
            log.info("MembershipManager closed for {}", nodeId);
        }
    }
}


