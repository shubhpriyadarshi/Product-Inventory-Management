package org.example.models;

public class Order {

    private int order_id; // order id
    private String product_id; // product id
    private int buyer_id; // buyer id
    private int quantity; // ordered quantity

    public Order(int orderId, String productId, int buyerId, int quantity) {
        this.order_id = orderId;
        this.product_id = productId;
        this.buyer_id = buyerId;
        this.quantity = quantity;
    }

    public Order(String productId, int buyerId, int quantity) {
        this.product_id = productId;
        this.buyer_id = buyerId;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return order_id;
    }

    public String getProductId() {
        return product_id;
    }

    public int getBuyerId() {
        return buyer_id;
    }

    public int getQuantity() {
        return quantity;
    }
}
