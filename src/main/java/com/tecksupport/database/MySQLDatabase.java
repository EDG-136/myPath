package com.tecksupport.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
        Connect();
    }

    void Connect() {
        try {
            connection = DriverManager.getConnection(
                    url,
                    sqlUsername,
                    sqlPassword
            );
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "MySQL can't connect!", e);
        }
    }

    Connection getConnection() {
        return connection;
    }
}
