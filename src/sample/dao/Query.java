package sample.dao;

// TODO: get postgresql imports and drivers

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;


/**
 * Executes statements initiated by the DBConnector class.
 *
 * Provides methods used across the application to run and execute
 * statements prepared via the DBConnector class.
 *
 * @author Jose Marquez
 */
public class Query {
    private static Statement statement;
    private static PreparedStatement preparedStatement;
    private static ResultSet result;

    /**
     * Runs a previously written SQL statement.
     *
     * @param q the string query to run
     */
    public static void runQuery(String q) {
        try {
            statement = DBConnector.newStatement();
            result = statement.executeQuery(q);
        } catch(Exception e) {
            System.out.println("Query Error: " + e.getMessage());
        }
    }

    /**
     * Runs <code>UPDATE</code>, <code>INSERT</code> and <code>DELETE</code>
     * statement.
     *
     * @param q the string query to run
     */
    public static void runUpdate(String q) {
        try {
            statement = DBConnector.newStatement();
            statement.executeUpdate(q);
        } catch(Exception e) {
            System.out.println("Query Error: " + e.getMessage());
        }
    }

    /**
     * Initiates a <code>PreparedStatement</code> within the <code>DBConnector</code>
     * class.
     *
     * @param q the prepared SQL statement
     * @return a <code>PreparedStatment</code> with pre-set fields
     * @see DBConnector#newPreparedStatement(String)
     */
    public static PreparedStatement pendingStatement(String q) {
        try {
            preparedStatement = DBConnector.newPreparedStatement(q);
        } catch (Exception e) {
            System.out.println("Prepared Query Error: " + e.getMessage());
        }

        return preparedStatement;
    }

    /**
     * Gets the <code>ResultSet</code> object containing query results.
     *
     * @return a <code>ResultSet</code> object
     */
    public static ResultSet getResults() {
        return result;
    }
}
