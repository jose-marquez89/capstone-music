package sample.utility;

public class ReportItem {
    private String name;
    private int amount;

    public ReportItem(String name, int amount) {
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
