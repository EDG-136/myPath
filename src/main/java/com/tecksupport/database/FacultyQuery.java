package com.tecksupport.database;

import com.tecksupport.schedulePlanner.Faculty;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacultyQuery {
    private final Connection connection;
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final HashMap<String, Faculty> facultyHashMap = new HashMap<>();

    public FacultyQuery(Connection connection) {
        this.connection = connection;
        List<Faculty> facultyList = getListOfAllFaculties();
        for (Faculty faculty : facultyList) {
            facultyHashMap.put(faculty.getAcronym(), faculty);
        }
    }

    public List<Faculty> getListOfAllFaculties() {
        if (connection == null)
            return null;
        try {
            String query = "SELECT FacultyID, Name, Description, Longitude, Latitude FROM Faculties;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();

            return getFacultiesFromExecutedStatement(statement);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "All faculties query error", e);
        }
        return null;
    }

    private List<Faculty> getFacultiesFromExecutedStatement(Statement statement) {
        List<Faculty> faculties = new ArrayList<>();
        try {
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                String acronym = resultSet.getString("FacultyID");
                String name = resultSet.getString("Name");
                String description = resultSet.getString("Description");
                double longitude = resultSet.getDouble("Longitude");
                double latitude = resultSet.getDouble("Latitude");

                Faculty faculty = new Faculty(acronym, name, description, longitude, latitude);
                faculties.add(faculty);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return faculties;
    }

    public Faculty getFacultyFromAcronym(String acronym) {
        return facultyHashMap.get(acronym);
    }

    public List<Faculty> getFacultyList() {
        return facultyHashMap.values().stream().toList();
    }
}
