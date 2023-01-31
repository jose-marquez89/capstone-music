package sample.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import sample.dao.DBConnector;
import sample.dao.Query;

import java.sql.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class QueryTest {

    @BeforeEach
    void setUp() throws SQLException {
        DBConnector.connect();
    }

    @AfterEach
    void tearDown() throws SQLException {
        DBConnector.closeConnection();
    }

    @Test
    void runQueries() throws SQLException {
        String testQuery1 = "SELECT * FROM employee;";
        Query.runQuery(testQuery1);

        ResultSet rs1 = Query.getResults();
        assertTrue(rs1.next());

    }
}