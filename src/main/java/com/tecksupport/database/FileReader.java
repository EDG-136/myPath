package com.tecksupport.database;

import org.apache.poi.ss.usermodel.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileReader {
    Connection connection;

    Logger logger = Logger.getLogger(getClass().getName());
    public FileReader(Connection connection) {
        this.connection = connection;
    }

    public void createTables() {

        try {
            Statement statement = connection.createStatement();

            String dropStudents = "DROP TABLE IF EXISTS Students;";
            String dropEnrollment = "DROP TABLE IF EXISTS Enrollments;";
            String dropFavoriteSchedule = "DROP TABLE IF EXISTS FavoriteSchedule;";
            String dropPositionConnects = "DROP TABLE IF EXISTS PositionConnects";
            String dropPositions = "DROP TABLE IF EXISTS Positions;";
            String dropFaculties = "DROP TABLE IF EXISTS Faculties;";
            String dropEntries = "DROP TABLE IF EXISTS Entries;";
            String dropSchedules = "DROP TABLE IF EXISTS Schedules;";
            String dropCourses = "DROP TABLE IF EXISTS Courses;";

            String courseTable = "CREATE TABLE IF NOT EXISTS Courses ("
                    + "CourseID INT(5) NOT NULL,"
                    + "CourseName VARCHAR(300),"
                    + "CourseSubject VARCHAR(4),"
                    + "CourseCatalog VARCHAR(6),"
                    + "CourseSection VARCHAR(4),"
                    + "CONSTRAINT CoursePK PRIMARY KEY (CourseID)"
                    + ");";
            String facultyTable = "CREATE TABLE IF NOT EXISTS Faculties (" +
                    "FacultyID VARCHAR(20) NOT NULL," +
                    "Name VARCHAR(100)," +
                    "Description VARCHAR(1000)," +
                    "Longitude DOUBLE," +
                    "Latitude DOUBLE," +
                    "CONSTRAINT FacultyPK PRIMARY KEY (FacultyID)" +
                    ");";
            String scheduleTable = "CREATE TABLE IF NOT EXISTS Schedules (" +
                    "CourseID INT(5) NOT NULL," +
                    "LocationName VARCHAR(20)," +
                    "FacultyID VARCHAR(20)," +
                    "StartTime TIME NOT NULL," +
                    "EndTime TIME," +
                    "Days VARCHAR(4)," +
                    "StartDate DATE NOT NULL," +
                    "EndDate DATE," +
                    "CONSTRAINT SchedulePK PRIMARY KEY (CourseID, LocationName, FacultyID, StartTime, Days, StartDate)," +
                    "CONSTRAINT CourseIDFK FOREIGN KEY (CourseID)" +
                    "REFERENCES Courses(CourseID)" +
                    "ON DELETE CASCADE," +
                    "CONSTRAINT FacultyIDFK FOREIGN KEY (FacultyID)" +
                    "REFERENCES Faculties(FacultyID)" +
                    "ON DELETE CASCADE" +
                    ");";
            String positionTable = "CREATE TABLE IF NOT EXISTS Positions ("
                    + "PositionID INT(10) NOT NULL, "
                    + "X FLOAT(10, 5), "
                    + "Y FLOAT(10, 5), "
                    + "Z FLOAT(10, 5), "
                    + "CONSTRAINT PositionPK PRIMARY KEY (PositionID)"
                    + ");";
            String positionConnectTable = "CREATE TABLE IF NOT EXISTS PositionConnects (" +
                    "FromPosition INT(10) NOT NULL," +
                    "ToPosition INT(10) NOT NULL," +
                    "CONSTRAINT fromPositionFK FOREIGN KEY (FromPosition)" +
                    "REFERENCES Positions(PositionID)," +
                    "CONSTRAINT toPositionFK FOREIGN KEY (ToPosition)" +
                    "REFERENCES Positions(PositionID)," +
                    "CONSTRAINT positionConnectPK PRIMARY KEY (FromPosition,ToPosition)" +
                    ");";
            String entriesTable = "CREATE TABLE IF NOT EXISTS Entries (" +
                    "FacultyID VARCHAR(20) NOT NULL," +
                    "PositionID INT(10) NOT NULL," +
                    "CONSTRAINT entriesPK PRIMARY KEY (FacultyID, PositionID)," +
                    "CONSTRAINT facultyFK FOREIGN KEY (FacultyID)" +
                    "REFERENCES Faculties(facultyID)," +
                    "CONSTRAINT positionFK FOREIGN KEY (PositionID)" +
                    "REFERENCES Positions(PositionID)" +
                    ");";
            String studentTable = "CREATE TABLE IF NOT EXISTS Students (" +
                    "StudentID INT(9) NOT NULL," +
                    "FirstName VARCHAR(50)," +
                    "LastName VARCHAR(50)," +
                    "UserName VARCHAR(30) NOT NULL," +
                    "HashedPassword VARCHAR(60) NOT NULL," +
                    "CONSTRAINT studentPK PRIMARY KEY (StudentID)" +
                    ");";

            statement.execute(dropFavoriteSchedule);
            statement.execute(dropEnrollment);
            statement.execute(dropSchedules);
            statement.execute(dropCourses);
            statement.execute(dropStudents);
            statement.execute(dropEntries);
            statement.execute(dropFaculties);
            statement.execute(dropPositionConnects);
            statement.execute(dropPositions);

            statement.execute(positionTable);
            statement.execute(positionConnectTable);
            statement.execute(facultyTable);
            statement.execute(entriesTable);
            statement.execute(studentTable);
            statement.execute(courseTable);
            statement.execute(scheduleTable);

            readFaculties();
            readCourse();
            readSchedules();
            readFacultyInfoCSV();
            readFacultyLongitudeAndLatitude();
            readNodesIntoPositionsTable();
            readNodesIDIntoEntriesTable();
            readNeighborNodesIntoConnectsTable();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error", e);
        }
    }

    public void readNeighborNodesIntoConnectsTable() {
        try(BufferedReader br = new BufferedReader(new java.io.FileReader("src/main/resources/dataFiles/neighborNodes.txt"))) {
            String line = br.readLine();

            StringBuilder fromQuery = new StringBuilder();
            StringBuilder toQuery = new StringBuilder();
            fromQuery.append("INSERT IGNORE INTO PositionConnects (FromPosition, ToPosition) VALUES ");
            toQuery.append("INSERT IGNORE INTO PositionConnects (ToPosition, FromPosition) VALUES ");
            while (line != null) {
                int fromPositionID = Integer.parseInt(line.substring(0, line.indexOf(':')));
                int toPositionID = Integer.parseInt(line.substring(line.indexOf(':') + 1));

                fromQuery.append("('").append(fromPositionID).append("','").append(toPositionID).append("'),");
                toQuery.append("('").append(fromPositionID).append("','").append(toPositionID).append("'),");
                line = br.readLine();
            }
            fromQuery.deleteCharAt(fromQuery.lastIndexOf(","));
            toQuery.deleteCharAt(toQuery.lastIndexOf(","));

            PreparedStatement fromStatement = connection.prepareStatement(fromQuery.toString());
            PreparedStatement toStatement = connection.prepareStatement(toQuery.toString());
            fromStatement.execute();
            toStatement.execute();

        } catch (IOException | SQLException e) {
            logger.log(Level.SEVERE, "SQL read faculties longitude and latitude error", e);
        }
    }

    public void readNodesIDIntoEntriesTable() {
        try(BufferedReader br = new BufferedReader(new java.io.FileReader("src/main/resources/dataFiles/entries.txt"))) {
            String line = br.readLine();

            StringBuilder query = new StringBuilder();
            query.append("INSERT INTO Entries (FacultyID, PositionID) VALUES ");
            while (line != null) {
                String facultyID = line.substring(0, line.indexOf(' '));
                int positionID = Integer.parseInt(line.substring(line.indexOf(' ') + 1));

                query.append("('").append(facultyID).append("',");
                query.append("'").append(positionID).append("'),");

                line = br.readLine();
            }
            query.deleteCharAt(query.lastIndexOf(","));

            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
            preparedStatement.execute();

        } catch (IOException | SQLException e) {
            logger.log(Level.SEVERE, "SQL read faculties longitude and latitude error", e);
        }
    }

    public void readNodesIntoPositionsTable() {
        try(BufferedReader br = new BufferedReader(new java.io.FileReader("src/main/resources/dataFiles/nodes.txt"))) {
            String line = br.readLine();

            StringBuilder query = new StringBuilder();
            query.append("INSERT INTO Positions (PositionID, x, y, z) VALUES ");
            while (line != null) {
                String id = line.substring(0, line.indexOf(':'));
                float x = Float.parseFloat(line.substring(line.indexOf(':') + 1, line.indexOf(',')));
                float y = Float.parseFloat(line.substring(line.indexOf(',') + 1, line.lastIndexOf(',')));
                float z = Float.parseFloat(line.substring(line.lastIndexOf(',') + 1));
                query.append("('").append(id).append("',");
                query.append("'").append(x).append("',");
                query.append("'").append(y).append("',");
                query.append("'").append(z).append("'),");

                line = br.readLine();
            }
            query.deleteCharAt(query.lastIndexOf(","));

            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
            preparedStatement.execute();

        } catch (IOException | SQLException e) {
            logger.log(Level.SEVERE, "SQL read faculties longitude and latitude error", e);
        }
    }

    public void readFacultyLongitudeAndLatitude() {
        try(BufferedReader br = new BufferedReader(new java.io.FileReader("src/main/resources/dataFiles/BuildingCoordinates.txt"))) {
            String line = br.readLine();


            while (line != null) {
                String query = ("UPDATE Faculties SET longitude = ?,  latitude = ? WHERE facultyID = ?;");
                String facultyID = line.substring(0, line.indexOf(':'));
                double longitude = Double.parseDouble(line.substring(line.indexOf('(') + 1, line.indexOf(',')));
                double latitude = Double.parseDouble(line.substring(line.indexOf(',') + 1, line.indexOf(')')));

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDouble(1, longitude);
                preparedStatement.setDouble(2, latitude);
                preparedStatement.setString(3, facultyID);

                preparedStatement.execute();

                line = br.readLine();
            }
        } catch (IOException | SQLException e) {
            logger.log(Level.SEVERE, "SQL read faculties longitude and latitude error", e);
        }
    }

    public void readFacultyInfoCSV() {
        try (BufferedReader br = new BufferedReader(new java.io.FileReader("src/main/resources/dataFiles/FacultyInfo.csv"))) {
            String line;
            List<String> list = new ArrayList<>();
            while((line = br.readLine()) != null) {
                list.addAll(Arrays.asList(line.split("\"")));
            }
            for (int i = 1; i + 4 < list.size(); i += 6) {
                String name = list.get(i);
                String acro = list.get(i + 1).replace("Acronym: ", "");
                String desc = list.get(i + 4);

                String query = "UPDATE Faculties SET name = ?, description = ? WHERE facultyID = ?;";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(3, acro);
                statement.setString(1, name);
                statement.setString(2, desc);
                statement.execute();
            }
        } catch (IOException | SQLException e) {
            logger.log(Level.SEVERE, "SQL read faculties info error", e);
        }
    }



    public void readCourse() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("dataFiles/course.xls")) {

            Workbook wb = WorkbookFactory.create(inputStream);
            Sheet sheet = wb.getSheetAt(0);
            // Course
            StringBuilder query = new StringBuilder("INSERT IGNORE INTO Courses (CourseID, CourseName, CourseSubject, CourseCatalog, CourseSection) VALUES\n");
            for (Row row : sheet) {
                if (row.getRowNum() < 2)
                    continue;
                String courseSubject = row.getCell(0).getStringCellValue();
                String courseCatalog = row.getCell(1).toString().trim();
                String courseSection = row.getCell(2).toString().trim();
                int courseNum = (int) row.getCell(3).getNumericCellValue();

                String courseName = row.getCell(5).getStringCellValue();
                String status = row.getCell(8).getStringCellValue();
                if (status.equalsIgnoreCase("CANCELLED"))
                    continue;

                String locationID = row.getCell(9).getStringCellValue();
                if (locationID.isEmpty())
                    continue;

                String days = row.getCell(10).getStringCellValue();

                DataFormatter formatter = new DataFormatter();
                FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

                SimpleDateFormat formattedTime = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat unformattedTime = new SimpleDateFormat("HH:mm:ss a");

                SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat unformattedDate = new SimpleDateFormat("MM/dd/yy");

                Cell cell11 = row.getCell(11);
                String startTime = null;
                if (cell11 != null) {
                    startTime = formatter.formatCellValue(cell11, evaluator);
                    java.util.Date time = unformattedTime.parse(startTime);
                    startTime = formattedTime.format(time);
                }

                if (startTime == null)
                    continue;

                Cell cell12 = row.getCell(12);
                String endTime = null;
                if (cell12 != null) {
                    endTime = formatter.formatCellValue(cell12, evaluator);
                    java.util.Date time = unformattedTime.parse(endTime);
                    endTime = formattedTime.format(time);
                }

                if (endTime == null)
                    continue;

                Cell cell14 = row.getCell(14);
                String startDate = null;
                if (cell14 != null) {
                    startDate = formatter.formatCellValue(cell14, evaluator);
                    java.util.Date time = unformattedDate.parse(startDate);
                    startDate = formattedDate.format(time);
                }

                Cell cell15 = row.getCell(15);
                String endDate = null;
                if (cell15 != null) {
                    endDate = formatter.formatCellValue(cell15, evaluator);
                    java.util.Date time = unformattedDate.parse(endDate);
                    endDate = formattedDate.format(time);
                }


                query.append("(");
                // Course Insert;
                query.append(AddCourse(courseNum, courseName, courseSubject, courseCatalog, courseSection));

                query.append("),\n");
            }
            int lastComma = query.lastIndexOf(",");
            query.deleteCharAt(lastComma);
            Statement statement = connection.createStatement();
            statement.execute(query.toString());

        } catch (IOException | SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public void readSchedules() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("dataFiles/course.xls")) {

            Workbook wb = WorkbookFactory.create(inputStream);
            Sheet sheet = wb.getSheetAt(0);
            // Course
            StringBuilder query = new StringBuilder("INSERT INTO Schedules (CourseID, LocationName, FacultyID, StartTime, EndTime, Days, StartDate, EndDate) VALUES\n");
            for (Row row : sheet) {
                if (row.getRowNum() < 2)
                    continue;
                int courseNum = (int) row.getCell(3).getNumericCellValue();

                String status = row.getCell(8).getStringCellValue();
                if (status.equalsIgnoreCase("CANCELLED"))
                    continue;

                String locationID = row.getCell(9).getStringCellValue();
                if (locationID.isEmpty())
                    continue;

                String days = row.getCell(10).getStringCellValue();

                DataFormatter formatter = new DataFormatter();
                FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

                SimpleDateFormat formattedTime = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat unformattedTime = new SimpleDateFormat("hh:mm:ss aa");

                SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat unformattedDate = new SimpleDateFormat("MM/dd/yy");

                Cell cell11 = row.getCell(11);
                String startTime = null;
                if (cell11 != null) {
                    startTime = formatter.formatCellValue(cell11, evaluator);
                    java.util.Date time = unformattedTime.parse(startTime);
                    startTime = formattedTime.format(time);
                }

                if (startTime == null)
                    continue;

                Cell cell12 = row.getCell(12);
                String endTime = null;
                if (cell12 != null) {
                    endTime = formatter.formatCellValue(cell12, evaluator);
                    java.util.Date time = unformattedTime.parse(endTime);
                    endTime = formattedTime.format(time);
                }

                if (endTime == null)
                    continue;

                Cell cell14 = row.getCell(14);
                String startDate = null;
                if (cell14 != null) {
                    startDate = formatter.formatCellValue(cell14, evaluator);
                    java.util.Date time = unformattedDate.parse(startDate);
                    startDate = formattedDate.format(time);
                }

                Cell cell15 = row.getCell(15);
                String endDate = null;
                if (cell15 != null) {
                    endDate = formatter.formatCellValue(cell15, evaluator);
                    java.util.Date time = unformattedDate.parse(endDate);
                    endDate = formattedDate.format(time);
                }


                query.append("(");
                // Schedules Insert;
                query.append(AddSchedule(courseNum, locationID, startTime, endTime, days, startDate, endDate));

                query.append("),\n");
            }
            int lastComma = query.lastIndexOf(",");
            query.replace(lastComma, lastComma + 1, ";");
            Statement statement = connection.createStatement();
            statement.execute(query.toString());

        } catch (IOException | SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public void readFaculties() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("dataFiles/course.xls")) {

            Workbook wb = WorkbookFactory.create(inputStream);
            Sheet sheet = wb.getSheetAt(0);
            // Course
            StringBuilder query = new StringBuilder("INSERT IGNORE INTO Faculties (FacultyID) VALUES\n");
            for (Row row : sheet) {
                if (row.getRowNum() < 2)
                    continue;
                String status = row.getCell(8).getStringCellValue();
                if (status.equalsIgnoreCase("CANCELLED"))
                    continue;

                String locationID = row.getCell(9).getStringCellValue();
                if (locationID.isEmpty())
                    continue;

                String days = row.getCell(10).getStringCellValue();

                DataFormatter formatter = new DataFormatter();
                FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

                SimpleDateFormat formattedTime = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat unformattedTime = new SimpleDateFormat("HH:mm:ss a");

                SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat unformattedDate = new SimpleDateFormat("MM/dd/yy");

                Cell cell11 = row.getCell(11);
                String startTime = null;
                if (cell11 != null) {
                    startTime = formatter.formatCellValue(cell11, evaluator);
                    java.util.Date time = unformattedTime.parse(startTime);
                    startTime = formattedTime.format(time);
                }

                if (startTime == null)
                    continue;

                Cell cell12 = row.getCell(12);
                String endTime = null;
                if (cell12 != null) {
                    endTime = formatter.formatCellValue(cell12, evaluator);
                    java.util.Date time = unformattedTime.parse(endTime);
                    endTime = formattedTime.format(time);
                }

                if (endTime == null)
                    continue;

                Cell cell14 = row.getCell(14);
                String startDate = null;
                if (cell14 != null) {
                    startDate = formatter.formatCellValue(cell14, evaluator);
                    java.util.Date time = unformattedDate.parse(startDate);
                    startDate = formattedDate.format(time);
                }

                Cell cell15 = row.getCell(15);
                String endDate = null;
                if (cell15 != null) {
                    endDate = formatter.formatCellValue(cell15, evaluator);
                    Date time = unformattedDate.parse(endDate);
                    endDate = formattedDate.format(time);
                }


                query.append("(");
                query.append(AddFaculty(locationID));

                query.append("),\n");
            }
            int lastComma = query.lastIndexOf(",");
            query.replace(lastComma, lastComma + 1, ";");
            Statement statement = connection.createStatement();
            statement.execute(query.toString());

        } catch (IOException | SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String AddCourse(int courseNum, String courseName, String courseSubject, String courseCatalog, String courseSection) {

        return "'" + courseNum + "'," +
                "'" + courseName.replace("'", "''") + "'," +
                "'" + courseSubject + "'," +
                "'" + courseCatalog + "'," +
                "'" + courseSection + "'";
    }

    public String AddSchedule(int courseNum, String locationID, String startTime, String endTime, String days, String startDate, String endDate) {
        StringBuilder query = new StringBuilder();
        query.append("'");
        query.append(courseNum);
        query.append("'");
        query.append(",");

        query.append("'");
        query.append(locationID);
        query.append("'");
        query.append(",");

        query.append("'");
        if (locationID.contains(" ")) {
            if (!locationID.trim().isEmpty()) {
                Pattern regex = Pattern.compile(" \\d+");
                Matcher matcher = regex.matcher(locationID);
                if (matcher.find()) {
                    query.append(locationID, 0, locationID.indexOf(matcher.group()));
                } else {
                    query.append(locationID);
                }
            }
        }
        else
            query.append(locationID);
        query.append("'");
        query.append(",");

        query.append("'");
        query.append(startTime);
        query.append("'");
        query.append(",");

        query.append("'");
        query.append(endTime);
        query.append("'");
        query.append(",");

        query.append("'");
        query.append(days);
        query.append("'");
        query.append(",");

        query.append("'");
        query.append(startDate);
        query.append("'");
        query.append(",");

        query.append("'");
        query.append(endDate);
        query.append("'");

        return query.toString();
    }

    public String AddFaculty(String locationID) {
        StringBuilder query = new StringBuilder();
        query.append("'");
        if (locationID.contains(" ")) {
            if (!locationID.trim().isEmpty()) {
                Pattern regex = Pattern.compile(" \\d+");
                Matcher matcher = regex.matcher(locationID);
                if (matcher.find()) {
                    query.append(locationID, 0, locationID.indexOf(matcher.group()));
                } else {
                    query.append(locationID);
                }
            }
        }
        else
            query.append(locationID);
        query.append("'");
        return query.toString();
    }
}
