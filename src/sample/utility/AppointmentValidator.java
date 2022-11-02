package sample.utility;

import sample.dao.DBConnector;
import sample.dao.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class AppointmentValidator {
    public static boolean validateStartEnd(LocalDateTime start, LocalDateTime end) {
        return start.isBefore(end);
    }
    public static boolean validateBusinessHours(LocalDateTime start, LocalDateTime end) {
        LocalTime estStart, estEnd;
        LocalTime businessStart = LocalTime.of(8, 0);
        LocalTime businessEnd = LocalTime.of(22, 0);
        boolean validated = false;

        estStart = start
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("America/New York"))
                .toLocalTime();
        estEnd = end
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("America/New York"))
                .toLocalTime();

        if (estStart.compareTo(businessStart) >= 0 && estEnd.compareTo(businessEnd) <= 0) {
            validated = true;
        }

        return validated;
    }

    public static boolean validateOverlap(LocalDateTime start, LocalDateTime end) throws SQLException {
        LocalDateTime maxExistingStart, maxExistingEnd;
        ResultSet results;
        boolean validated;
        String queryParam = end.toString();
        String query = String.format("""
                SELECT 
                    start AS latest_start,
                    end AS latest_end
                FROM appointments
                WHERE start < %s
                ORDER BY start DESC
                LIMIT 1;
                """);

        DBConnector.connect();
        Query.runQuery(query);
        results = Query.getResults();

        if (results.next()) {
            maxExistingStart = results.getTimestamp("latest_start").toLocalDateTime();
            maxExistingEnd = results.getTimestamp("latest_end").toLocalDateTime();

            if (maxExistingStart.isBefore(start) && maxExistingEnd.isBefore(end)) {
                validated = true;
            } else {
                validated = false;
            }
        } else {
            validated = true;
        }

        return validated;
    }
}
