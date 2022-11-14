package sample.utility;

/**
 * Models the record used in the report displaying
 * amount of appointments by month and type.
 *
 * @author Jose Marquez
 */
public class MonthTypeRecord extends ReportRecord {
    private String subName;

    /**
     * Constructor for the <code>MonthTypeRecord</code> class
     *
     * @param name the month of the aggregate record
     * @param subName the type of the aggregate record
     * @param amount the amount associated with the aggregate record
     */
    public MonthTypeRecord(String name, String subName, int amount) {
        super(name, amount);
        this.subName = subName;
    }

    /**
     * Gets the sub-name of the aggregate record.
     *
     * While not directly used in the code, this method
     * is used to retrieve the sub-name in the <code>TableView</code> that
     * displays <code>MonthTypeRecord</code> objects.
     *
     * @return a string representing the sub-name
     */
    public String getSubName() {
        return subName;
    }
}
