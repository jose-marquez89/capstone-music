package sample.dao;

import javax.xml.transform.Result;
import java.sql.Statement;
import java.sql.ResultSet;


public class Query {
    private static String query;
    private static Statement statement;
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

    public static ResultSet getResults() {
        return result;
    }
}
