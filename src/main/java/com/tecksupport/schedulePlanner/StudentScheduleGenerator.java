package com.tecksupport.schedulePlanner;

import com.tecksupport.database.FacultyQuery;
import com.tecksupport.glfw.pathfinder.OSM.ORSAPI;
import com.tecksupport.glfw.pathfinder.Route.RouteSummary;

import java.util.*;

public class StudentScheduleGenerator {
    private final FacultyQuery facultyQuery;
    private final List<StudentSchedules> savedSchedules = new ArrayList<>();
    private final List<GeneralCourse> selectedGeneralCourses = new ArrayList<>();

    public StudentScheduleGenerator(FacultyQuery facultyQuery) {
        this.facultyQuery = facultyQuery;
    }

    public List<StudentSchedules> generateSchedule() {
        List<StudentSchedules> studentSchedulesList = new ArrayList<>();
        generateScheduleIntoList(studentSchedulesList, new StudentSchedules(), 0);
        return studentSchedulesList;
    }

    public void generateScheduleIntoList(List<StudentSchedules> schedulesList, StudentSchedules studentSchedules, int i) {
        if (i >= selectedGeneralCourses.size()) {
            if (schedulesList.size() > 10) {
                return;
            }
            RouteSummary routeSummary = ORSAPI.getRouteSummary(getFacultiesPerDay(studentSchedules));
            studentSchedules.setRouteSummary(routeSummary);
            schedulesList.add(studentSchedules);
            return;
        }

        GeneralCourse currentCourse = selectedGeneralCourses.get(i);
        List<CourseSection> courseSectionList = currentCourse.getCourseSectionList();
        i++;
        // Creating a branch for every course section in current general course
        for (CourseSection courseSection : courseSectionList) {
            // Create a copy of student schedules
            StudentSchedules branchSchedule = new StudentSchedules(studentSchedules);

            // Stop branching if failed to add (overlap detected)
            if (!branchSchedule.addCourseSection(courseSection))
                continue;

            generateScheduleIntoList(schedulesList, branchSchedule, i);
        }
    }

    private HashMap<EDayInWeek, List<Faculty>> getFacultiesPerDay(StudentSchedules studentSchedules) {
        HashMap<EDayInWeek, List<Faculty>> facultyHashMap = new HashMap<>();
        for (CourseSection courseSection : studentSchedules.getCourseSectionList()) {
            for (Schedule schedule : courseSection.getSchedules()) {
                char[] daysInWeek = schedule.getDaysInWeek().toCharArray();
                for (char dayInChar : daysInWeek) {
                    EDayInWeek dayInWeek = StudentSchedules.getDayInWeek(dayInChar);
                    List<Faculty> facultyList = facultyHashMap.computeIfAbsent(dayInWeek, k -> new ArrayList<>());
                    Faculty faculty = facultyQuery.getFacultyFromAcronym(schedule.getFacultyID());
                    facultyList.add(faculty);
                }
            }
        }
        return facultyHashMap;
    }

    private void getFacultyList(EDayInWeek dayInWeek) {
    }

    public void addGeneralCourse(GeneralCourse generalCourse) {
        selectedGeneralCourses.add(generalCourse);
    }

    public void removeGeneralCourse(GeneralCourse generalCourse) {
        selectedGeneralCourses.remove(generalCourse);
    }

    public void addSavedSchedule(StudentSchedules studentSchedules) {
        savedSchedules.add(studentSchedules);
    }

    public void removeSavedSchedule(StudentSchedules studentSchedules) {
        savedSchedules.remove(studentSchedules);
    }

    public void clearGeneralCourse() {
        selectedGeneralCourses.clear();
    }
}
