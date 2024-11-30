package com.tecksupport.pathfinder.OSM;

public class OSMNode {
    private final long id;
    private final double latitude;
    private final double longitude;

    public OSMNode(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.format("OSMNode{id=%d, lat=%.6f, lon=%.6f}", id, latitude, longitude);
    }
}