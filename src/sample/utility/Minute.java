package sample.utility;

/**
 * Models an integer minute with the ability to
 * display zero-padded single digit minutes as part
 * of a traditional clock display (HH:MM)
 *
 * @author Jose Marquez
 */
public class Minute {
    private int integerMinute;
    private String textMinute;

    /**
     * Constructor for the <code>Minute</code> class
     *
     * @param im the integer representation of a minute
     * @param tm the string representation of a minute
     */
    public Minute(int im, String tm) {
        integerMinute = im;
        textMinute = tm;
    }

    public int getIntegerMinute() {
        return integerMinute;
    }
    public String getTextMinute() {
       return textMinute;
    }
}
