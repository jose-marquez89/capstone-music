package sample.controller;

import sample.model.Order;

public class ReturnConfigCtrl {
    private Order selectedOrder;

    public void setSelectedOrder(Order targetOrder) {
        selectedOrder = targetOrder;
    }
}
