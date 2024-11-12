package com.tecksupport.database;

<<<<<<< Updated upstream
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
=======
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
>>>>>>> Stashed changes
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLDatabase {
    private final String url;
    private final String sqlUsername;
    private final String sqlPassword;

    private Connection connection;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public MySQLDatabase(String url, String sqlUsername, String sqlPassword) {
        this.url = "jdbc:mysql://" + url;
        this.sqlUsername = sqlUsername;
        this.sqlPassword = sqlPassword;
<<<<<<< Updated upstream
        Connect();
    }

    void Connect() {
        try {
=======
    }

    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
>>>>>>> Stashed changes
            connection = DriverManager.getConnection(
                    url,
                    sqlUsername,
                    sqlPassword
            );
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL can't connect!", e);
<<<<<<< Updated upstream
=======
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "JDBC Driver not found!", e);
>>>>>>> Stashed changes
        }
    }

    Connection getConnection() {
        return connection;
    }
<<<<<<< Updated upstream
=======

    public CourseInfo getCourseInfo(int courseID) {
        try {
            String query = "SELECT * FROM Course " +
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

                return new CourseInfo(courseID, courseName, courseSubject, courseCatalog, courseSection);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL Get Class Info Error!", e);
        }
        return null;
    }

    public List<Schedule> getScheduleOfCourse(int courseID) {
        try {
            String query = "SELECT * FROM Schedule " +
                    "WHERE CourseID = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, courseID);
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            List<Schedule> schedules = new ArrayList<>();
            while (resultSet.next()) {
                String facultyID = resultSet.getString("FacultyID");
                String startTime = resultSet.getString("StartTime");
                String endTime = resultSet.getString("EndTime");
                String startDate = resultSet.getString("StartDate");
                String daysInWeek = resultSet.getString("Days");
                String endDate = resultSet.getString("EndDate");
                Schedule schedule = new Schedule(courseID, facultyID, startTime, endTime, daysInWeek, startDate, endDate);
                schedules.add(schedule);
            }
            return schedules;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL Get Schedule Info From Course ID Error!", e);
            return null;
        }
    }

    public List<Schedule> getSchedulesOfSubjectAndCatalog(String subject, String catalog) {
        try {
            String query = "SELECT CourseID, FacultyID, StartTime, EndTime, Days, StartDate, EndDate " +
                    "FROM Course, Schedule " +
                    "WHERE Course.CourseID = Schedule.CourseID " +
                    "AND CourseSubject = ? " +
                    "AND CourseCatalog = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, subject);
            statement.setString(2, catalog);
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            List<Schedule> schedules = new ArrayList<>();
            while (resultSet.next())
            {
                int courseID = resultSet.getInt("CourseID");
                String facultyID = resultSet.getString("FacultyID");
                String startTime = resultSet.getString( "StartTime");
                String endTime = resultSet.getString("EndTime");
                String startDate = resultSet.getString("StartDate");
                String daysInWeek = resultSet.getString("Days");
                String endDate = resultSet.getString("EndDate");

                Schedule schedule = new Schedule(courseID, facultyID, startTime, endTime, daysInWeek, startDate, endDate);
                schedules.add(schedule);
            }
            return schedules;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL Get Schedule Info From Catalog Error!", e);
            return null;
        }
    }

    public void Close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
>>>>>>> Stashed changes
}
