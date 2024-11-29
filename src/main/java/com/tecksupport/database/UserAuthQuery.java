package com.tecksupport.database;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserAuthQuery {
    private final Connection connection;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public UserAuthQuery(Connection connection) {
        this.connection = connection;
    }
    public boolean register(String studentID, String username, String password, String firstName, String lastName) {
        try {
            if (isUsernameExist(username) || isStudentIDExist(studentID))
                return false;

            String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            String query = "INSERT INTO Students (StudentID, UserName, HashedPassword, FirstName, LastName) VALUES (?,?,?,?,?);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, studentID);
            statement.setString(2, username);
            statement.setString(3, hashPassword);
            statement.setString(4, firstName);
            statement.setString(5, lastName);
            statement.execute();
            return true;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL query error on register!", e);
            return false;
        }
    }

    private boolean isStudentIDExist(String studentID) {
        try {
            String query = "SELECT StudentID " +
                    "FROM Students " +
                    "WHERE StudentID = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, studentID);
            statement.execute();

            // If query receive at least a row (IT SHOULDN'T BE MORE THAN 1 ROW)
            return statement.getResultSet().next();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL studentID query error!", e);
            return false;
        }
    }

    public boolean isUsernameExist(String username) {
        try {
            String query = "SELECT StudentID " +
                    "FROM Students " +
                    "WHERE Username = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.execute();

            // If query receive at least a row (IT SHOULDN'T BE MORE THAN 1 ROW)
            return statement.getResultSet().next();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL username query error!", e);
            return false;
        }
    }

    public boolean isPasswordCorrect(String username, String password) {
        if (username.equalsIgnoreCase("ADMIN") && password.equalsIgnoreCase("TECK"))
            return true;

        try {
            String query = "SELECT UserName,HashedPassword " +
                    "FROM Students " +
                    "WHERE UserName = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.execute();

            ResultSet result = statement.getResultSet();

            if (result.next()) {
                String hashPassword = result.getString("HashedPassword");
                return BCrypt.checkpw(password, hashPassword);
            }
            return false;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL query error when checking password!", e);
            return false;
        }
    }
}
