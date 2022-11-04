package sample.utility;

import sample.dao.DBConnector;
import sample.dao.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;

public class AppointmentValidator {
    public static boolean validateStartEnd(LocalDateTime start, LocalDateTime end) {
        return start.isBefore(end);
    }
    public static boolean validateBusinessHours(LocalDateTime start, LocalDateTime end) {
        LocalTime estStart, estEnd;
        LocalTime businessStart = LocalTime.of(8, 0);
        LocalTime businessEnd = LocalTime.of(22, 0);

        // meetings longer than 14 hours automatically fall outside of biz hours
        if (Duration.between(start, end).toHours() > 14.0) {
            return false;
        };

        estStart = start
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("America/New_York"))
                .toLocalTime();
        estEnd = end
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("America/New_York"))
                .toLocalTime();

        if (estStart.compareTo(businessStart) >= 0 && estEnd.compareTo(businessEnd) <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateOverlap(LocalDateTime start, LocalDateTime end) throws SQLException {
        LocalDateTime maxExistingStart, maxExistingEnd;
        ResultSet results;
        boolean validated;
        String queryParam = end
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC)
                .toString();
        String query = String.format("""
                SELECT 
                    appointment_id,
                    start AS latest_start,
                    end AS latest_end
                FROM appointments
                WHERE start < '%s'
                ORDER BY start DESC
                LIMIT 1;
                """, queryParam);

        DBConnector.connect();
        Query.runQuery(query);
        results = Query.getResults();

        if (results.next()) {
            maxExistingStart = results.getTimestamp("latest_start").toLocalDateTime();
            maxExistingEnd = results.getTimestamp("latest_end").toLocalDateTime();

            if (maxExistingStart.compareTo(start) <= 0 && maxExistingEnd.compareTo(start) <= 0) {
                validated = true;
            } else {
                validated = false;
            }
        } else {
            validated = true;
        }

        DBConnector.closeConnection();
        return validated;
    }

    public static boolean validateOverlap(LocalDateTime start, LocalDateTime end, int apptId) throws SQLException {
        LocalDateTime maxExistingStart, maxExistingEnd;
        ResultSet results;
        boolean validated;
        String queryParam = end
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC)
                .toString();
        String query = String.format("""
                SELECT 
                    appointment_id,
                    start AS latest_start,
                    end AS latest_end
                FROM appointments
                WHERE start < '%s'
                    AND appointment_id <> %s
                ORDER BY start DESC
                LIMIT 1;
                """, queryParam, apptId);

        DBConnector.connect();
        Query.runQuery(query);
        results = Query.getResults();

        if (results.next()) {
            maxExistingStart = results.getTimestamp("latest_start").toLocalDateTime();
            maxExistingEnd = results.getTimestamp("latest_end").toLocalDateTime();

            if (maxExistingStart.compareTo(start) <= 0 && maxExistingEnd.compareTo(start) <= 0) {
                validated = true;
            } else {
                validated = false;
            }
        } else {
            validated = true;
        }

        DBConnector.closeConnection();
        return validated;
    }
}
