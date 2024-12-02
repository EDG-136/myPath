package com.tecksupport.schedulePlanner;

public class Faculty {
    private final String acronym;
    private final String name;
    private final String description;
    private final double longitude;
    private final double latitude;

    public Faculty(String acronym, String name, String description, double longitude, double latitude) {
        this.acronym = acronym;
        this.name = name;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getAcronym() {
        return acronym;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
