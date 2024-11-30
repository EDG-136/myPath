package com.tecksupport.database;

import com.tecksupport.schedulePlanner.CourseSection;
import com.tecksupport.schedulePlanner.GeneralCourse;
import com.tecksupport.schedulePlanner.Schedule;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseQuery {
    private static final long QUERY_WAIT_TIME_IN_MILLIS = 30000;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Connection connection;
    private final List<CourseSection> courseSectionList;
    private final List<GeneralCourse> generalCourseList;
    private final List<Schedule> scheduleList;
    private long lastGeneralCourseQueryTime;
    private long lastCourseSectionQueryTime;

    public CourseQuery(Connection connection) {
        this.connection = connection;

        scheduleList = getAllSchedules();
        courseSectionList = queryCourseSectionList();
        generalCourseList = queryGeneralCourseList();

        addSchedulesToCourseSections();
        addCourseSectionsToGeneralCourseList();
    }

    public List<CourseSection> queryCourseSectionList() {
        try {
            String query = "SELECT * FROM Courses;";

            PreparedStatement statement = connection.prepareStatement(query);

            statement.execute();

            return getCourseListFromStatement(statement);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL All course query error!", e);
        }
        return null;
    }

    public CourseSection getCourseInfo(int courseID) {
        try {
            String query = "SELECT * FROM Courses " +
                    "WHERE CourseID = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, courseID);
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                String courseName = resultSet.getString("CourseName");
                String courseSubject = resultSet.getString("CourseSubject");
                String courseCatalog = resultSet.getString("CourseCatalog");
                String courseSection = resultSet.getString("CourseSection");

                return new CourseSection(courseID, courseName, courseSubject, courseCatalog, courseSection);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL Get Class Info Error!", e);
        }
        return null;
    }

    private List<CourseSection> getCourseListFromStatement(Statement statement) {
        List<CourseSection> courseSectionList = new ArrayList<>();
        try {
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int courseID = resultSet.getInt("CourseID");
                String courseName = resultSet.getString("CourseName");
                String courseSubject = resultSet.getString("CourseSubject");
                String courseCatalog = resultSet.getString("CourseCatalog");
                String courseSection = resultSet.getString("CourseSection");

                CourseSection course = new CourseSection(courseID, courseName, courseSubject, courseCatalog, courseSection);
                courseSectionList.add(course);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Course list collect error", e);
        }

        return courseSectionList;
    }

    public List<Schedule> getAllSchedules() {
        try {
            String query = "SELECT CourseID, LocationName, FacultyID, StartTime, EndTime, Days, StartDate, EndDate " +
                    "FROM Schedules;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();

            return getScheduleList(statement);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL Get ALl Schedules Error!", e);
        }
        return null;
    }

    public List<Schedule> getScheduleOfCourse(int courseID) {
        try {
            String query = "SELECT CourseID, LocationName, FacultyID, StartTime, EndTime, Days, StartDate, EndDate" +
                    "FROM Schedules " +
                    "WHERE CourseID = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, courseID);
            statement.execute();

            return getScheduleList(statement);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL Get Schedule Info From Course ID Error!", e);
            return null;
        }
    }

    public List<Schedule> getSchedulesOfSubjectAndCatalog(String subject, String catalog) {
        try {
            String query = "SELECT CourseID, LocationName, FacultyID, StartTime, EndTime, Days, StartDate, EndDate " +
                    "FROM Courses, Schedules " +
                    "WHERE Course.CourseID = Schedule.CourseID " +
                    "AND CourseSubject = ? " +
                    "AND CourseCatalog = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, subject);
            statement.setString(2, catalog);
            statement.execute();

            return getScheduleList(statement);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL Get Schedule Info From Catalog Error!", e);
            return null;
        }
    }

    private static @NotNull List<Schedule> getScheduleList(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.getResultSet();
        List<Schedule> schedules = new ArrayList<>();
        while (resultSet.next())
        {
            int courseID = resultSet.getInt("CourseID");
            String roomName = resultSet.getString("LocationName");
            String facultyID = resultSet.getString("FacultyID");
            String startTime = resultSet.getString( "StartTime");
            String endTime = resultSet.getString("EndTime");
            String startDate = resultSet.getString("StartDate");
            String daysInWeek = resultSet.getString("Days");
            String endDate = resultSet.getString("EndDate");

            Schedule schedule = new Schedule(courseID, roomName, facultyID, startTime, endTime, daysInWeek, startDate, endDate);
            schedules.add(schedule);
        }
        return schedules;
    }

    public List<GeneralCourse> queryGeneralCourseList() {
        List<GeneralCourse> generalCourses = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT CourseSubject, CourseCatalog FROM Courses;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                String subject = resultSet.getString("CourseSubject");
                String catalog = resultSet.getString("CourseCatalog");
                GeneralCourse generalCourse = new GeneralCourse(subject, catalog);
                generalCourses.add(generalCourse);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL All General Course query error!", e);
        }

        return generalCourses;
    }

    public List<GeneralCourse> getGeneralCourseList() {
        return generalCourseList;
    }

    public List<CourseSection> getCourseSectionList() {
        return courseSectionList;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    private void addSchedulesToCourseSections() {
        for (Schedule schedule : scheduleList) {
            for (CourseSection courseSection : courseSectionList) {
                if (schedule.getCourseID() != courseSection.getID())
                    continue;

                courseSection.addSchedule(schedule);
                break;
            }
        }
    }

    private void addCourseSectionsToGeneralCourseList() {
        for (CourseSection courseSection : courseSectionList) {
            for (GeneralCourse generalCourse : generalCourseList) {
                if (!isCourseSectionInGeneralCourse(courseSection, generalCourse))
                    continue;

                generalCourse.addSection(courseSection);
                break;
            }
        }
    }

    private boolean isCourseSectionInGeneralCourse(CourseSection courseSection, GeneralCourse generalCourse) {
        return courseSection.getSubject().equals(generalCourse.getSubject())
                && courseSection.getCatalog().equals(generalCourse.getCatalog());
    }
}
