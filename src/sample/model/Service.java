package sample.model;

import java.util.ArrayList;

public class Service extends Item {
    private ArrayList<String> associatedProducts;

    public Service(int id, String name, double price) {
        super(id, name, price);
        associatedProducts = new ArrayList<String>();
    }

    public void addAssociatedProduct(String productName) {
        associatedProducts.add(productName);
    }

    public ArrayList<String> getAssociatedProducts() {
        return associatedProducts;
    }
}
