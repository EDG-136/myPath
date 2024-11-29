package com.tecksupport.schedulePlanner;

public class Faculty {
    private final String acronym;
    private final String name;
    private final String description;

    public Faculty(String acronym, String name, String description) {
        this.acronym = acronym;
        this.name = name;
        this.description = description;
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
}
