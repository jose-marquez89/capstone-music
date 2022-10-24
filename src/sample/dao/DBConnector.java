package sample.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/client_schedule";
    private static final String UNAME = "sqlUser";
    private static final String PWORD = "Passw0rd!";

    private static Connection conn;

    public static void connect() throws SQLException {
        conn = (Connection) DriverManager.getConnection(DB_URL, UNAME, PWORD);
    }

    public static void closeConnection() throws SQLException {
        conn.close();
    }

    public static Statement newStatement() throws SQLException {
        return conn.createStatement();
    }
}
