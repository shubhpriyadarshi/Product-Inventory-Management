package org.example.models;

public class Product {

    private String product_id; // unique product id
    private String product_name; // product name
    private int in_stock_quantity; // quantity in stock

    public Product(String product_id, String product_name, int in_stock_quantity) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.in_stock_quantity = in_stock_quantity;
    }

    public String getProductId() {
        return product_id;
    }

    public String getProductName() {
        return product_name;
    }

    public int getInStockQuantity() {
        return in_stock_quantity;
    }

    public void setInStockQuantity(int in_stock_quantity) {
        this.in_stock_quantity = in_stock_quantity;
    }

    public String getName() {
        return product_name;
    }

    public void setId(int generatedId) {
        this.product_id = product_id;
    }
}