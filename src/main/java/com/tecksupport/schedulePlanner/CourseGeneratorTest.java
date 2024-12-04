package com.tecksupport.schedulePlanner;

import com.tecksupport.database.FacultyQuery;
import com.tecksupport.database.MySQLDatabase;
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
        MySQLDatabase database = new MySQLDatabase("localhost/TeckSupportDB", "client", "TeckSupport");
        database.connect();
        generator = new StudentScheduleGenerator(new FacultyQuery(database.getConnection()));
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

        Schedule schedule3 = new Schedule(23452, "ACD 209", "ACD", "10:30:00", "11:45:00", "TR", "2024-08-30", "2024-12-10");
        Schedule schedule4 = new Schedule(23452, "SCI2 302", "SCI2", "12:00:00", "01:15:00", "TR", "2024-08-30", "2024-12-10");

        Schedule schedule5 = new Schedule(29324, "SCI2 302", "SCI2", "08:00:00", "13:30:00", "TR", "2024-08-30", "2024-12-10");

        Schedule schedule6 = new Schedule(28323, "SCI2 302", "SCI2", "08:00:00", "09:30:00", "TR", "2024-08-30", "2024-12-10");

        cs370Section1.addSchedule(schedule1);
        cs370Section1.addSchedule(schedule2);

        cs370Section2.addSchedule(schedule3);
        cs370Section2.addSchedule(schedule4);

        cs436Section1.addSchedule(schedule5);
        cs436Section2.addSchedule(schedule6);

        cs370.addSection(cs370Section1);
        cs370.addSection(cs370Section2);

        cs436.addSection(cs436Section1);
        cs436.addSection(cs436Section2);

        generator.addGeneralCourse(cs370);
        generator.addGeneralCourse(cs436);

        List<StudentSchedules> studentSchedulesList = generator.generateSchedule();

        for (StudentSchedules studentSchedules : studentSchedulesList) {
            System.out.println(String.format("%10s", "-").replace(' ', '-'));
            for (CourseSection courseSection : studentSchedules.getCourseSectionList()) {
                System.out.println(courseSection.getID());
            }
        }
    }
}
