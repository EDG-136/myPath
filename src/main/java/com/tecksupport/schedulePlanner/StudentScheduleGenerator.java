package com.tecksupport.schedulePlanner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class StudentScheduleGenerator {
    private final List<GeneralCourse> selectedGeneralCourses = new ArrayList<>();

    public List<StudentSchedules> generateSchedule() {
        List<StudentSchedules> studentSchedulesList = new ArrayList<>();
        generateScheduleIntoList(studentSchedulesList, new StudentSchedules(), selectedGeneralCourses.iterator());
        return studentSchedulesList;
    }

    public void generateScheduleIntoList(List<StudentSchedules> schedulesList, StudentSchedules studentSchedules, Iterator<GeneralCourse> currentCourse) {
        if (!currentCourse.hasNext()) {
            schedulesList.add(studentSchedules);
            return;
        }

        GeneralCourse generalCourse = currentCourse.next();
        Set<CourseSection> courseSections = generalCourse.getCourses();

        // Creating a branch for every course section in current general course
        for (CourseSection courseSection : courseSections) {
            // Create a copy of student schedules
            StudentSchedules branchSchedule = new StudentSchedules(studentSchedules);
            List<Schedule> schedulesToAdd = generalCourse.getSchedules(courseSection);

            // Stop branching if failed to add (overlap detected)
            if (!branchSchedule.addSchedules(schedulesToAdd))
                continue;

            generateScheduleIntoList(schedulesList, branchSchedule, currentCourse);
        }
    }

    public void addGeneralCourse(GeneralCourse generalCourse) {
        selectedGeneralCourses.add(generalCourse);
    }

    public void removeGeneralCourse(GeneralCourse generalCourse) {
        selectedGeneralCourses.remove(generalCourse);
    }
}
