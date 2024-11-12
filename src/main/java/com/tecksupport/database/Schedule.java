package com.tecksupport.database;

public class Schedule {
    private final int courseID;
    private final String facultyID;
    private final String startTime;
    private final String endTime;
    private final String daysInWeek;
    private final String startDate;
    private final String endDate;

    public Schedule(int courseID, String facultyID, String startTime, String endTime, String daysInWeek, String startDate, String endDate) {
        this.courseID = courseID;
        this.facultyID = facultyID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.daysInWeek = daysInWeek;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getCourseID() {
        return courseID;
    }

    public String getFacultyID() {
        return facultyID;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDaysInWeek() {
        return daysInWeek;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
