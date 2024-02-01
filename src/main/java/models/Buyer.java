package models;

public class Buyer {

    private int buyer_id; // buyer id
    private String name; // buyer name

    public Buyer(int buyerId, String name) {
        this.buyer_id = buyerId;
        this.name = name;
    }

    public int getBuyerId() {
        return buyer_id;
    }

    public String getName() {
        return name;
    }
}