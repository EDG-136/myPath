package com.tecksupport.schedulePlanner;

import java.util.*;

public class GeneralCourse {
    private final String subject;
    private final String catalog;
    private final Map<CourseSection, List<Schedule>> schedules = new HashMap<>();

    public GeneralCourse(String subject, String catalog) {
        this.subject = subject;
        this.catalog = catalog;
    }

    public void addSchedule(CourseSection courseSection, List<Schedule> schedules) {
        this.schedules.put(courseSection, schedules);
    }

    public String getSubject() {
        return subject;
    }

    public String getCatalog() {
        return catalog;
    }

    public Set<CourseSection> getCourses() {
        return schedules.keySet();
    }

    public List<Schedule> getSchedules(CourseSection courseSection) {
        return schedules.get(courseSection);
    }
}
