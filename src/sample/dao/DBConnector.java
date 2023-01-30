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
    private static final String DB_URL = "jdbc:mysql://" + System.getenv("DB_CONN");
    private static final String UNAME = System.getenv("USERNAME");
    private static final String PWORD = System.getenv("PASSWORD");

    private static Connection conn;

    /**
     * Creates the connection to the MySQL database.
     *
     * Requires components of a URL string, including a password,
     * to connect.
     *
     * @throws SQLException
     */
    public static void connect() throws SQLException {
        conn = (Connection) DriverManager.getConnection(DB_URL, UNAME, PWORD);
    }

    /**
     * Terminates the MySQL connection opened by the <code>connect</code> method.
     *
     * @throws SQLException
     */
    public static void closeConnection() throws SQLException {
        conn.close();
    }

    /**
     * Creates a new SQL statement on behalf of the method caller.
     *
     * @return a SQL <code>Statement</code> object
     * @throws SQLException
     */
    public static Statement newStatement() throws SQLException {
        return conn.createStatement();
    }

    /**
     * Creates a prepared statement to use with parameters in a SQL query.
     *
     * Useful for avoiding SQL injection when commands may be entered in
     * text fields.
     *
     * @param statement a string SQL statement
     * @return a <code>PreparedStatement</code> object to be executed
     * @throws SQLException
     */
    public static PreparedStatement newPreparedStatement(String statement) throws SQLException {
        return conn.prepareStatement(statement);
    }
}
