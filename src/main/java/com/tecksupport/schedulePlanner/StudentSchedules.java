package com.tecksupport.schedulePlanner;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StudentSchedules {
    private static final int DAYS_IN_A_WEEK = 7;
    private static final int MONDAY = 0;
    private static final int TUESDAY = 1;
    private static final int WEDNESDAY = 2;
    private static final int THURSDAY = 3;
    private static final int FRIDAY = 4;
    private static final int SATURDAY = 5;
    private static final int SUNDAY = 6;
    List<List<Schedule>> schedulesInWeek = new ArrayList<>();

    public StudentSchedules() {
        // Initialize each day to an empty list of Schedules
        for (int i = 0; i < DAYS_IN_A_WEEK; i++) {
            schedulesInWeek.add(new LinkedList<>());
        }
    }

    public StudentSchedules(StudentSchedules studentSchedules) {
        for (int i = 0; i < DAYS_IN_A_WEEK; i++) {
            List<Schedule> schedules = new LinkedList<>(studentSchedules.schedulesInWeek.get(i));
            this.schedulesInWeek.add(schedules);
        }
    }

    public boolean addSchedules(List<Schedule> schedules) {
        if (isSchedulesOverlap(schedules)) {
            return false;
        }

        for (Schedule schedule : schedules) {
            addSchedule(schedule);
        }

        return true;
    }

    // Adding the schedule without checking overlap
    private void addSchedule(Schedule schedule) {
        char[] days = schedule.getDaysInWeek().toCharArray();

        for (char dayChar : days) {
            int dayInWeek = getDayInWeek(dayChar);
            List<Schedule> schedules = schedulesInWeek.get(dayInWeek);
            schedules.addLast(schedule);
        }
    }

    private boolean isSchedulesOverlap(List<Schedule> schedules) {
        for (Schedule schedule : schedules) {
            if (isScheduleOverlap(schedule))
                return true;
        }
        return false;
    }

    private boolean isScheduleOverlap(Schedule schedule) {
        // The excel got a letter for each day in a week
        char[] days = schedule.getDaysInWeek().toCharArray();
        // Checking if schedule overlap with any days it is covering
        for (char dayChar : days) {
            int dayInWeek = getDayInWeek(dayChar);
            List<Schedule> scheduleList = schedulesInWeek.get(dayInWeek);
            if (isScheduleOverlap(schedule, scheduleList))
                return true;
        }
        return false;
    }

    private static boolean isScheduleOverlap(Schedule newSchedule, List<Schedule> scheduleList) {
        // The code should explain itself
        for (Schedule currentSchedule : scheduleList) {

            if (isTimeBetween(newSchedule.getStartTime(), currentSchedule))
                return true;

            if (isTimeBetween(newSchedule.getEndTime(), currentSchedule))
                return true;

            if (isTimeBetween(currentSchedule.getStartTime(), newSchedule))
                return true;

            if (isTimeBetween(currentSchedule.getEndTime(), newSchedule))
                return true;
        }
        return false;
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
}
