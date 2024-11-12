package com.tecksupport.database;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserAuthentication {
    private final MySQLDatabase mySQL;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public UserAuthentication(MySQLDatabase mySQL) {
        this.mySQL = mySQL;
    }

    public boolean register(String username, String password) {
        try {
            if (isUsernameExist(username))
                return false;

            String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            String query = "INSERT INTO Student (StudentID, StudentName, HashedPassword) VALUES (?,?);";
            PreparedStatement statement = mySQL.getConnection().prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, hashPassword);
            statement.execute();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL query error on register!", e);
        }

        return true;
    }

    public boolean isUsernameExist(String username) {
        try {
            String query = "SELECT StudentID,HashedPassword " +
                    "FROM Student " +
                    "WHERE StudentID = ? ";
            PreparedStatement statement = mySQL.getConnection().prepareStatement(query);
            statement.setString(1, username);
            return statement.execute();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL username query error!", e);
        }
        return false;
    }

    public boolean isPasswordCorrect(String username, String password) {
        try {
            String query = "SELECT StudentID,HashedPassword " +
                    "FROM Student " +
                    "WHERE StudentID = ? " +
                    "AND HashedPassword = ?;";
            PreparedStatement statement = mySQL.getConnection().prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.execute();

            ResultSet result = statement.getResultSet();

            if (result.next()) {
                String hashPassword = result.getString("HashedPassword");
                return BCrypt.checkpw(password, hashPassword);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL query error when checking password!", e);
        }

        return false;
    }
}
