package com.tecksupport.schedulePlanner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Source;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class CourseGeneratorTest {
    private StudentScheduleGenerator generator;

    @BeforeEach
    public void init() {
        generator = new StudentScheduleGenerator();
    }

    @Test
    public void addingSelectedCourseTest() {
        GeneralCourse cs370 = new GeneralCourse("CS", "370");
        GeneralCourse cs436 = new GeneralCourse("CS", "436");

        CourseSection cs370Section1 = new CourseSection(12345, "Intro to Software Engineering", "CS", "370", "10");
        CourseSection cs370Section2 = new CourseSection(23452, "Intro to Software Engineering", "CS", "370", "21A");

        CourseSection cs436Section1 = new CourseSection(29324, "Intro to Networking", "CS", "436", "10");
        CourseSection cs436Section2 = new CourseSection(28323, "Intro to Networking", "CS", "436", "20");

        Schedule schedule1 = new Schedule(12345, "SCI2 302", "SCI2", "08:00:00", "09:20:00", "TR", "2024-08-30", "2024-12-10");
        Schedule schedule2 = new Schedule(12345, "SCI2 302", "SCI2", "09:30:00", "10:15:00", "TR", "2024-08-30", "2024-12-10");

        Schedule schedule3 = new Schedule(23452, "ACD 209", "ACD", "08:00:00", "10:15:00", "TR", "2024-08-30", "2024-12-10");
        Schedule schedule4 = new Schedule(23452, "SCI2 302", "SCI2", "09:00:00", "10:15:00", "MWF", "2024-08-30", "2024-12-10");

        Schedule schedule5 = new Schedule(20932, "SCI2 302", "SCI2", "08:00:00", "01:30:00", "F", "2024-08-30", "2024-12-10");

        Schedule schedule6 = new Schedule(43298, "SCI2 302", "SCI2", "08:00:00", "09:30:00", "MWF", "2024-08-30", "2024-12-10");

//        cs370.addSchedule(cs370Section1, List.of(schedule1, schedule2));
//        cs370.addSchedule(cs370Section2, List.of(schedule3, schedule4));
//
//        cs436.addSchedule(cs436Section1, List.of(schedule5));
//        cs436.addSchedule(cs436Section2, List.of(schedule6));

        generator.addGeneralCourse(cs370);
        generator.addGeneralCourse(cs436);

        List<StudentSchedules> studentSchedulesList = generator.generateSchedule();

        for (StudentSchedules studentSchedules : studentSchedulesList) {
            System.out.println(String.format("%10s", "-").replace(' ', '-'));
            for (int i = 0; i < 7; i++) {
                System.out.println(getDayInWeek(i) + ": ");
                for (CourseSection courseSection : studentSchedules.getCourseSectionList()) {
                    System.out.println(courseSection.getID());
                }
            }
        }
    }

    private String getDayInWeek(int dayInNum) {
        return switch (dayInNum) {
            case 0 -> "MONDAY";
            case 1 -> "TUESDAY";
            case 2 -> "WEDNESDAY";
            case 3 -> "THURSDAY";
            case 4 -> "FRIDAY";
            case 5 -> "SATURDAY";
            case 6 -> "SUNDAY";
            default -> null;
        };
    }

}
