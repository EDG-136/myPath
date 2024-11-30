package com.tecksupport.schedulePlanner;

import java.util.*;

public class GeneralCourse {
    private final String subject;
    private final String catalog;
    private final List<CourseSection> courseSectionList = new ArrayList<>();

    public GeneralCourse(String subject, String catalog) {
        this.subject = subject;
        this.catalog = catalog;
    }

    public void addSection(CourseSection courseSection) {
        courseSectionList.add(courseSection);
    }

    public String getSubject() {
        return subject;
    }

    public String getCatalog() {
        return catalog;
    }

    public List<CourseSection> getCourseSectionList() {
        return courseSectionList;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GeneralCourse generalCourse)) {
            return false;
        }

        return this.subject.equals(generalCourse.getSubject())
                && this.catalog.equals(generalCourse.getCatalog());
    }
}
