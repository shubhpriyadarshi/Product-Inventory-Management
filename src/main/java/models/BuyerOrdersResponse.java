package models;

public class BuyerOrdersResponse {

    private String product_id;
    private String product_name;
    private int quantity;

    public BuyerOrdersResponse(String productName, String productId, int quantity) {
        this.product_name = productName;
        this.product_id = productId;
        this.quantity = quantity;
    }
}