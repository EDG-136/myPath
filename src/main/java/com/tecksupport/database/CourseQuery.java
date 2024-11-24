package com.tecksupport.database;

import com.tecksupport.database.data.Course;
import com.tecksupport.database.data.Schedule;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseQuery {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Connection connection;

    public CourseQuery(Connection connection) {
        this.connection = connection;
    }

    public Course getCourseInfo(int courseID) {
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

                return new Course(courseID, courseName, courseSubject, courseCatalog, courseSection);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL Get Class Info Error!", e);
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

}
