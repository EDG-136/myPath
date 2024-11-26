package com.tecksupport.database;

import com.tecksupport.database.data.Faculty;
import org.lwjgl.system.linux.Stat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacultyQuery {
    private final Connection connection;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public FacultyQuery(Connection connection) {
        this.connection = connection;
    }

    public List<Faculty> getListOfAllFaculties() {
        if (connection == null)
            return null;
        try {
            String query = "SELECT FacultyID, Name, Description FROM Faculties;";
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

                Faculty faculty = new Faculty(acronym, name, description);
                faculties.add(faculty);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return faculties;
    }
}
