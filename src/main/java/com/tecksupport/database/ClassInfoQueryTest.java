package com.tecksupport.database;

import com.tecksupport.schedulePlanner.CourseSection;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassInfoQueryTest {
    private static MySQLDatabase database;
    private static CourseQuery courseQuery;

    @BeforeAll
    public static void init() {
        database = new MySQLDatabase("localhost/TeckSupportDB", "client", "CS370TeckSupport");
        database.connect();
        
        courseQuery = new CourseQuery(database.getConnection());
    }
    @Test
    public void classInfoQueryTest1() {
        CourseSection info = courseQuery.getCourseInfo(42280);

        database.close();

        CourseSection correctInfo = new CourseSection(
                42280,
                "INTRO SOFTWARE ENGR",
                "CS",
                "370",
                "20"
        );
        compareCourseInfo(info, correctInfo);
    }

    void compareCourseInfo(CourseSection info, CourseSection actual) {
        assertEquals(info.getID(), actual.getID());
        assertEquals(info.getName(), actual.getName());
        assertEquals(info.getCatalog(), actual.getCatalog());
        assertEquals(info.getSection(), actual.getSection());
        assertEquals(info.getSubject(), actual.getSubject());
    }
}
