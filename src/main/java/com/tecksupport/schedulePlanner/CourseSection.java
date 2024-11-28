package com.tecksupport.schedulePlanner;


public class CourseSection {
    private final int ID;
    private final String name;
    private final String subject;
    private final String catalog;
    private final String section;

    public CourseSection(int ID, String name, String subject, String catalog, String section) {
        this.ID = ID;
        this.name = name.trim();
        this.subject = subject.trim();
        this.catalog = catalog.trim();
        this.section = section.trim();
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSection() {
        return section;
    }

    @Override
    public int hashCode() {
        return getID();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CourseSection courseSection))
            return false;

        return getID() == courseSection.getID();
    }
}