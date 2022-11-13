package sample.utility;

import sample.dao.DBConnector;
import sample.dao.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;

/**
 * Helps validate appointments in methods used in the dashboard
 * controller.
 *
 * Provides methods to validate appointment times and dates.
 *
 * @author Jose Marquez
 */
public class AppointmentValidator {
    /**
     * Validates that input start date/time is before input end date/time.
     *
     * @param start the <code>LocalDateTime</code> object representing appointment start date/time
     * @param end the <code>LocalDateTime</code> object representing appointment end date/time
     * @return true if start and end date/times are valid
     */
    public static boolean validateStartEnd(LocalDateTime start, LocalDateTime end) {
        return start.isBefore(end);
    }

    /**
     * Determines whether the entered dates are within business hours.
     *
     * Business hours are validated against 8am-10pm EST.
     *
     * @param start the <code>LocalDateTime</code> object representing appointment start date/time
     * @param end the <code>LocalDateTime</code> object representing appointment end date/time
     * @return true if appointment date/time is within business hours
     */
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

    /**
     * Determines whether the entered start and end date/time overlaps with that of an
     * existing appointment.
     *
     * Overlap is defined here as the start or end of an existing appointment happening
     * <em>after</em> the start of a potential appointment.
     *
     * @param start the <code>LocalDateTime</code> object representing appointment start date/time
     * @param end the <code>LocalDateTime</code> object representing appointment end date/time
     * @return true if pending appointment start/end date/time does not overlap
     * @throws SQLException
     */
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

    /**
     * Validates overlap of appointment times for the appointment update form.
     *
     * Requires an appointment id to ignore the bounds of the appointment
     * being updated.
     *
     * @param start the <code>LocalDateTime</code> object representing appointment start date/time
     * @param end the <code>LocalDateTime</code> object representing appointment end date/time
     * @param apptId the integer appointment id of the appointment to be updated
     * @return true if the new appointment start and end date/time do not overlap
     * @throws SQLException
     */
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
