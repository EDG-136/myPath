package com.tecksupport.schedulePlanner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class StudentScheduleGenerator {
    private final List<StudentSchedules> savedSchedules = new ArrayList<>();
    private final List<GeneralCourse> selectedGeneralCourses = new ArrayList<>();

    public List<StudentSchedules> generateSchedule() {
        List<StudentSchedules> studentSchedulesList = new ArrayList<>();
        generateScheduleIntoList(studentSchedulesList, new StudentSchedules(), 0);
        return studentSchedulesList;
    }

    public void generateScheduleIntoList(List<StudentSchedules> schedulesList, StudentSchedules studentSchedules, int i) {
        if (i >= selectedGeneralCourses.size()) {
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
