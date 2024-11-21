package com.tecksupport.Pathfinder;

import java.util.ArrayList;
import java.util.List;

public class OSMWay {
    private long id;
    private List<Long> nodeRefs;

    public OSMWay(long id) {
        this.id = id;
        this.nodeRefs = new ArrayList<>();
    }

    public void addNodeRef(long ref) {
        nodeRefs.add(ref);
    }

    public long getId() {
        return id;
    }

    public List<Long> getNodeRefs() {
        return nodeRefs;
    }
}