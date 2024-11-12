package com.tecksupport.database;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClassInfoQueryTest {
    MySQLDatabase database;

    @Test
    public void ClassInfoQueryTest1() {
        database = new MySQLDatabase("localhost/TeckSupportDB", "client", "CS370TeckSupport");
        database.Connect();

        CourseInfo info = database.getCourseInfo(42280);

        database.Close();

        CourseInfo correctInfo = new CourseInfo(
                42280,
                "INTRO SOFTWARE ENGR",
                "CS",
                "370",
                "20"
        );
        compareCourseInfo(info, correctInfo);
    }

    void compareCourseInfo(CourseInfo info, CourseInfo actual) {
        assertEquals(info.getCourseID(), actual.getCourseID());
        assertEquals(info.getCourseName(), actual.getCourseName());
        assertEquals(info.getCourseCatalog(), actual.getCourseCatalog());
        assertEquals(info.getCourseSection(), actual.getCourseSection());
        assertEquals(info.getCourseSubject(), actual.getCourseSubject());
    }
}
