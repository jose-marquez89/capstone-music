package sample.utility;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class AppointmentValidator {
    public static boolean validateStartEnd(LocalDateTime start, LocalDateTime end) {
        return start.isBefore(end);
    }
    public static boolean validateOverlap(LocalDateTime start, LocalDateTime end) {
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
}
