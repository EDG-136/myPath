package com.tecksupport.Pathfinder;

public class OSMNode {
    private long id;
    private double latitude;
    private double longitude;

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
}