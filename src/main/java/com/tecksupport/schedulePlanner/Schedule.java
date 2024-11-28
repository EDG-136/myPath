package com.tecksupport.schedulePlanner;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private final int courseID;
    private final String roomName;
    private final String facultyID;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String daysInWeek;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Schedule(int courseID, String roomName, String facultyID, String startTime, String endTime, String daysInWeek, String startDate, String endDate) {
        this.courseID = courseID;
        this.roomName = roomName;
        this.facultyID = facultyID;
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
        this.daysInWeek = daysInWeek;
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
    }

    public int getCourseID() {
        return courseID;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getFacultyID() {
        return facultyID;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getDaysInWeek() {
        return daysInWeek;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
