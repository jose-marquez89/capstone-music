package sample.dao;

import java.sql.*;

/**
 * Creates connections to the MySQL database across
 * the application.
 *
 * Opens and closes connections, and prepares statements for the Query class.
 *
 * @author Jose Marquez
 */
public class DBConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/client_schedule?connectionTimeZone=SERVER";
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

    public static PreparedStatement newPreparedStatement(String statement) throws SQLException {
        return conn.prepareStatement(statement);
    }
}
