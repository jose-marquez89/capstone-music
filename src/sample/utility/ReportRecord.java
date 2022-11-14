package sample.utility;

/**
 * An abstract model of report records that are displayed in
 * report tables on the "Reports" tab of the main user interface.
 *
 * @author Jose Marquez
 */
abstract public class ReportRecord {
    private String name;
    private int amount;

    /**
     * Constructor for the <code>ReportRecord</code> class.
     *
     * @param name the aggregation record name
     * @param amount the aggregation record amount
     */
    public ReportRecord(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
