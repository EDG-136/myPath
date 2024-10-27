package com.tecksupport.database;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserAuthentication {
    public final String TABLE_NAME = "UserAuthInfo";
    public final String USERNAME_COLUMN = "auth_username";
    public final String PASSWORD_COLUMN = "auth_password";

    private final MySQLDatabase mySQL;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public UserAuthentication(MySQLDatabase mySQL) {
        this.mySQL = mySQL;
    }

    public boolean register(String username, String password) {
        try {
            if (isUsernameExist(username))
                return false;

            Statement statement = mySQL.getConnection().createStatement();
            String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            String query = "INSERT INTO " + TABLE_NAME + " (" + USERNAME_COLUMN + ", " + PASSWORD_COLUMN + ")"
                    + "VALUES (" + username + ", " + hashPassword + ");";

            statement.executeUpdate(query);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL query error on register!", e);
        }

        return true;
    }

    public boolean isUsernameExist(String username) {
        try {
            Statement statement = mySQL.getConnection().createStatement();
            String query = "SELECT " + USERNAME_COLUMN + " FROM " + TABLE_NAME + " WHERE " + USERNAME_COLUMN + " = " + username + ";";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next())
                return true;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL username query error!", e);
        }
        return false;
    }

    public boolean isPasswordCorrect(String username, String password) {
        try {
            Statement statement = mySQL.getConnection().createStatement();
            String query = "SELECT " + PASSWORD_COLUMN + " FROM " + TABLE_NAME + " WHERE " + USERNAME_COLUMN + " = " + username + ";";

            ResultSet result = statement.executeQuery(query);

            if (result.next()) {
                String hashPassword = result.getString("auth_password");
                return BCrypt.checkpw(password, hashPassword);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL query error when checking password!", e);
        }

        return false;
    }
}
