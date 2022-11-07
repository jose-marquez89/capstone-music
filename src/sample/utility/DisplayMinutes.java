package sample.utility;

/**
 * Creates a collection of Minute objects formatted for display.
 *
 * This class creates a set of Minute objects formatted
 * to display minutes in mm format in the appointments forms.
 *
 * @author Jose Marquez
 */
public class DisplayMinutes {
    private static final int minuteSlots = 12;
    private static Minute[] minutes = new Minute[minuteSlots];
    public static void initializeMinutes() {
        for (int i=0; i < minuteSlots; i++) {
            String display;
            if ((i * 5) < 10) {
                display = "0" + Integer.toString(i * 5);
            } else {
                display = Integer.toString(i * 5);
            }
            minutes[i] = new Minute(i * 5, display);
        }
    }
    public static Minute[] getMinutes() {
        return minutes;
    }
}
