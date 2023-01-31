package sample.test;

import org.junit.jupiter.api.*;
import sample.dao.DBConnector;
import sample.dao.Query;

import java.math.BigDecimal;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class QueryTest {

    private static int oid;

    @BeforeAll
    static void setUp() throws SQLException {
        DBConnector.connect();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        String deleteQuery1 = String.format("""
                DELETE FROM order_line
                WHERE order_id = %d
                """, oid);
        String deleteQuery2 = String.format("""
                DELETE FROM "order"
                WHERE id = %d
                """, oid);

        Query.runUpdate(deleteQuery1);
        Query.runUpdate(deleteQuery2);
        DBConnector.closeConnection();
    }

    @Test
    void runQuery() throws SQLException {
        String testQuery1 = "SELECT * FROM employee;";
        Query.runQuery(testQuery1);

        ResultSet rs1 = Query.getResults();
        assertTrue(rs1.next());
    }

    @Test
    void runPreparedQuery() throws SQLException {
        BigDecimal amount = null;
        // Donny T buys a grand piano in NYC
        String orderQuery = """
            INSERT INTO "order" (customer_id, processing_employee, origin_store_id)
            VALUES (?, ?, ?);
            """;

        PreparedStatement testPs = Query.pendingStatement(orderQuery);
        testPs.setInt(1, 3);
        testPs.setInt(2, 9);
        testPs.setInt(3, 3);
        testPs.executeUpdate();

        // get the id of the most recent order
        String idQuery = """
                SELECT id AS oid
                FROM "order"
                WHERE customer_id = 3 AND created_at = (SELECT MAX(created_at) FROM "order")
                LIMIT 1; 
                """;

        Query.runQuery(idQuery);
        ResultSet idRs = Query.getResults();
        if (idRs.next()) {
            oid = idRs.getInt("oid");
        }

        String lineQuery = """
                INSERT INTO order_line (is_service, product_id, service_id, order_id, returned_at) 
                VALUES (FALSE, ?, NULL, ?, NULL);
                """;

        PreparedStatement olPs = Query.pendingStatement(lineQuery);
        olPs.setInt(1, 4);
        olPs.setInt(2, oid);
        olPs.executeUpdate();

        String testQuery1 = String.format("""
                SELECT SUM(price) AS amount
                FROM order_line l
                LEFT JOIN product p
                ON l.product_id = p.id
                WHERE l.order_id = %d;
                """, oid);
        Query.runQuery(testQuery1);
        ResultSet testResult = Query.getResults();

        if (testResult.next()) {
            amount = testResult.getBigDecimal("amount");
        }

        assertTrue(amount.equals(BigDecimal.valueOf(15499.99)));
    }
}