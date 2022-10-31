package sample.utility;

public class Minute {
    private int integerMinute;
    private String textMinute;

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
