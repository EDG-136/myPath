package com.tecksupport.schedulePlanner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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



    }
}
