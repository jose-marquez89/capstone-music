package sample.model;

public class Product extends Item {
    private int quantityOnHand;

    public Product(int id, String name, double price, int quantityOnHand) {
        super(id, name, price);
        this.quantityOnHand = quantityOnHand;
    }

    public int getQuantityOnHand() {
        return quantityOnHand;
    }
}
