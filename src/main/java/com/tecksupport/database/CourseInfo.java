package com.tecksupport.database;

public class CourseInfo {
    private final int courseID;
    private final String courseName;
    private final String courseSubject;
    private final String courseCatalog;
    private final String courseSection;

    public CourseInfo(int courseID, String courseName, String courseSubject, String courseCatalog, String courseSection) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseSubject = courseSubject;
        this.courseCatalog = courseCatalog.replace(" ", "");
        this.courseSection = courseSection;
    }

    public int getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseSubject() {
        return courseSubject;
    }

    public String getCourseCatalog() {
        return courseCatalog;
    }

    public String getCourseSection() {
        return courseSection;
    }
}
