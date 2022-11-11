package sample.utility;

import java.time.Month;

/**
 * Models the record used in the report displaying
 * amount of appointments by month and type.
 *
 * @author Jose Marquez
 */
public class MonthTypeRecord extends ReportRecord {
    private String subName;

    public MonthTypeRecord(String name, String subName, int amount) {
        super(name, amount);
        this.subName = subName;
    }

    public String getSubName() {
        return subName;
    }
}
