package sample.dao;

import com.mysql.cj.x.protobuf.MysqlxPrepare;

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
    private static String query;
    private static Statement statement;
    private static PreparedStatement preparedStatement;
    private static ResultSet result;

    public static void runQuery(String q) {
        try {
            statement = DBConnector.newStatement();
            result = statement.executeQuery(q);
        } catch(Exception e) {
            System.out.println("Query Error: " + e.getMessage());
        }
    }

    public static void runUpdate(String q) {
        try {
            statement = DBConnector.newStatement();
            statement.executeUpdate(q);
        } catch(Exception e) {
            System.out.println("Query Error: " + e.getMessage());
        }
    }

    public static PreparedStatement pendingStatement(String q) {
        try {
            preparedStatement = DBConnector.newPreparedStatement(q);
        } catch (Exception e) {
            System.out.println("Prepared Query Error: " + e.getMessage());
        }

        return preparedStatement;
    }

    public static void executePendingQuery(PreparedStatement ps) {
        try {
            result = ps.executeQuery();
        } catch (Exception e) {
            System.out.println("Prepared Query Error: " + e.getMessage());
        }
    }

    public static void executePendingUpdate(PreparedStatement ps) {
        try {
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Prepared Query Error: " + e.getMessage());
        }
    }

    public static ResultSet getResults() {
        return result;
    }
}
