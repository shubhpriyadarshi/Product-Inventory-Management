package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.models.Buyer;
import org.example.models.Order;
import org.example.models.Product;
import org.example.models.BuyerOrdersResponse;
import org.example.exceptions.InsufficientStockException;

public class DBUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "mysql";

    private static final String ADD_PRODUCT = "insert into product(product_name, in_stock_quantity) values (?, ?)";
    private static final String UPDATE_PRODUCT = "update product set in_stock_quantity = in_stock_quantity + ? where product_name = ?";

    private static final String ADD_OR_UPDATE_PRODUCT = "insert into product(product_name, in_stock_quantity) values (?, ?) on duplicate key update in_stock_quantity = in_stock_quantity + VALUES(in_stock_quantity)";
    private static final String GET_PRODUCT = "select * from product where product_id = ?";
    private static final String GET_PRODUCT_BY_NAME = "select * from product where product_name = ?";
    private static final String ADD_BUYER = "insert into buyer(name) values (?)";
    private static final String PLACE_ORDER = "insert into `order` (product_id, buyer_id, quantity) values (?, ?, ?)";
    private static final String GET_BUYERS_AND_PRODUCTS = "SELECT b.name, p.product_name, p.product_id, sum(o.quantity) " +
            "FROM buyer b " +
            "JOIN `order` o ON b.buyer_id = o.buyer_id " +
            "JOIN product p ON o.product_id = p.product_id " +
            "GROUP BY b.name, p.product_name, p.product_id " +
            "ORDER BY b.name";


    private DBUtil() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void addProduct(Product product) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement psSelect = conn.prepareStatement(GET_PRODUCT_BY_NAME);
             PreparedStatement psInsert = conn.prepareStatement(ADD_PRODUCT);
             PreparedStatement psUpdate = conn.prepareStatement(UPDATE_PRODUCT)) {

            psSelect.setString(1, product.getProductName());
            try (ResultSet rs = psSelect.executeQuery()) {
                if (rs.next()) {
                    psUpdate.setInt(1, product.getInStockQuantity());
                    psUpdate.setString(2, product.getProductName());
                    psUpdate.executeUpdate();
                } else {
                    psInsert.setString(1, product.getProductName());
                    psInsert.setInt(2, product.getInStockQuantity());
                    psInsert.executeUpdate();
                }
            }
        }
    }

    public static Product getProduct(String productId) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_PRODUCT)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(rs.getString(1), rs.getString(2), rs.getInt(3));
                } else {
                    return null;
                }
            }
        }
    }


    public static Product getProductByName(String productId) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_PRODUCT_BY_NAME)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(rs.getString(1), rs.getString(2), rs.getInt(3));
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addBuyer(Buyer buyer) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(ADD_BUYER)) {
            ps.setString(1, buyer.getName());
            ps.executeUpdate();
        }
    }

    public static Exception placeOrder(Order order) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(PLACE_ORDER)) {
            Product product = getProduct(order.getProductId());
            if (product != null) {
                if (product.getInStockQuantity() >= order.getQuantity()) {
                    ps.setString(1, order.getProductId());
                    ps.setInt(2, order.getBuyerId());
                    ps.setInt(3, order.getQuantity());
                    ps.executeUpdate();
                    product.setInStockQuantity(product.getInStockQuantity() - order.getQuantity());
                    addProduct(product);
                    return null;
                } else {
                    return new InsufficientStockException("Insufficient stock for product: " + product.getName());
                }
            }
            return new Exception("Product not found.");
        }
    }

    public static Map<String, List<BuyerOrdersResponse>> getBuyersAndTheirProducts() throws SQLException {
        Map<String, List<BuyerOrdersResponse>> buyersAndTheirProducts = new HashMap<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BUYERS_AND_PRODUCTS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String buyerName = rs.getString(1);
                BuyerOrdersResponse buyerOrdersResponse = new BuyerOrdersResponse(rs.getString(2), rs.getString(3), rs.getInt(4));
                buyersAndTheirProducts.computeIfAbsent(buyerName, k -> new ArrayList<>()).add(buyerOrdersResponse);
            }
        }
        return buyersAndTheirProducts;
    }
}