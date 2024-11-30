package com.tecksupport.schedulePlanner;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentSchedules {
    private static final int MONDAY = 0;
    private static final int TUESDAY = 1;
    private static final int WEDNESDAY = 2;
    private static final int THURSDAY = 3;
    private static final int FRIDAY = 4;
    private static final int SATURDAY = 5;
    private static final int SUNDAY = 6;
    private final List<CourseSection> courseSectionList = new ArrayList<>();

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
            System.out.println(schedule.getCourseID() +  " to " + currentSchedule.getCourseID());
            if (!matcher.find())
                continue;
            System.out.println(schedule.getCourseID() +  " Matched " + currentSchedule.getCourseID());

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

    private static int getDayInWeek(char letter) {
        // IDK why the school chose these letters
        // Email to them if you care
        return switch (letter) {
            case 'M' -> MONDAY;
            case 'T' -> TUESDAY;
            case 'W' -> WEDNESDAY;
            case 'R' -> THURSDAY;
            case 'F' -> FRIDAY;
            case 'S' -> SATURDAY;
            case 'U' -> SUNDAY;
            default -> -1;
        };
    }

    public List<CourseSection> getCourseSectionList() {
        return courseSectionList;
    }
}
