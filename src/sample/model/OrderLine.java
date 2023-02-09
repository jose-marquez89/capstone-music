package sample.model;

public class OrderLine extends Item {
    boolean isService;

    public OrderLine(int id, String name, double price, boolean isService) {
        super(id, name, price);
        this.isService = isService;
    }

    public boolean isService() {
        return isService;
    }
}
