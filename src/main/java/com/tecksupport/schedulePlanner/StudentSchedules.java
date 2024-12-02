package com.tecksupport.schedulePlanner;

import com.tecksupport.glfw.pathfinder.OSM.RouteSummary;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentSchedules {
    private final List<CourseSection> courseSectionList = new ArrayList<>();
    private RouteSummary routeSummary;

    // Had   to keep this for the first empty schedule
    public StudentSchedules() {

    }

    public StudentSchedules(StudentSchedules studentSchedules) {
        courseSectionList.addAll(studentSchedules.getCourseSectionList());
    }

    public boolean addCourseSection(CourseSection newCourseSection) {
        for (CourseSection currentCourseSection : courseSectionList) {
            for (Schedule schedule : newCourseSection.getSchedules()) {
                if (isScheduleOverlap(schedule, currentCourseSection.getSchedules()))
                    return false;
            }
        }

        courseSectionList.add(newCourseSection);
        return true;
    }

    private static boolean isScheduleOverlap(Schedule schedule, List<Schedule> scheduleList) {
        for (Schedule currentSchedule : scheduleList) {
            Pattern regex = Pattern.compile("[" + schedule.getDaysInWeek() + "]");
            Matcher matcher = regex.matcher(currentSchedule.getDaysInWeek());
            if (!matcher.find())
                continue;

            if (isScheduleOverlap(schedule, currentSchedule))
                return true;
        }
        return false;
    }

    private static boolean isScheduleOverlap(Schedule newSchedule, Schedule currentSchedule) {
        // The code should explain itself
        return isTimeBetween(newSchedule.getStartTime(), currentSchedule)
                || isTimeBetween(newSchedule.getEndTime(), currentSchedule)
                || isTimeBetween(currentSchedule.getStartTime(), newSchedule)
                || isTimeBetween(currentSchedule.getEndTime(), newSchedule);
    }
    private static boolean isTimeBetween(LocalTime time, Schedule schedule) {
        // Confusing isn't it?
        // just read code without "get" and it would make sense (perchance)
        return time.isAfter(schedule.getStartTime()) && time.isBefore(schedule.getEndTime());
    }

    public static EDayInWeek getDayInWeek(char letter) {
        // IDK why the school chose these letters
        // Email to them if you care
        return switch (letter) {
            case 'M' -> EDayInWeek.MONDAY;
            case 'T' -> EDayInWeek.TUESDAY;
            case 'W' -> EDayInWeek.WEDNESDAY;
            case 'R' -> EDayInWeek.THURSDAY;
            case 'F' -> EDayInWeek.FRIDAY;
            case 'S' -> EDayInWeek.SATURDAY;
            case 'U' -> EDayInWeek.SUNDAY;
            default -> throw new IllegalStateException("Unexpected value: " + letter);
        };
    }

    public List<CourseSection> getCourseSectionList() {
        return courseSectionList;
    }

    public RouteSummary getRouteSummary() {
        return routeSummary;
    }

    public void setRouteSummary(RouteSummary routeSummary) {
        this.routeSummary = routeSummary;
    }
}

