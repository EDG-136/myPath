package com.tecksupport.glfw.pathfinder.Route;

public class Route {
    private final double distance;
    private final double time;

    public Route(double distance, double time) {
        this.distance = distance;
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public double getTime() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("Distance = %.2f meters, Time = %.2f seconds", distance, time);
    }
}
