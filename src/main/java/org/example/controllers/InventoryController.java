package org.example.controllers;

import com.google.gson.Gson;
import org.example.exceptions.OrderPlacementException;
import org.example.models.*;
import org.example.utils.DBUtil;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.io.PrintWriter;
import java.util.Map;

import static spark.Spark.*;

public class InventoryController extends IOException {
    static final PrintWriter pw = new PrintWriter(System.out);

    @Override
    protected void finalize() {
        pw.close();
    }

    public static String productToJson(Product product) {
        Gson gson = new Gson();
        return gson.toJson(product);
    }

    public static String buyerToJson(Buyer buyer) {
        Gson gson = new Gson();
        return gson.toJson(buyer);
    }

    public static String addProduct(Request request, Response response) {
        try {
            Product product = new Gson().fromJson(request.body(), Product.class);
            DBUtil.addProduct(product);
            response.status(200);
            response.type("application/json");
            product = DBUtil.getProductByName(product.getProductName());
            return productToJson(product);
        } catch (SQLException e) {
            response.status(500);
            return e.getMessage();
        } catch (Exception e) {
            response.status(400);
            return e.getMessage();
        }
    }

    public static String getStockInHand(Request request, Response response) {
        try {
            String productId = request.params(":productId");
            Product product = DBUtil.getProduct(productId);
            if (product != null) {
                response.status(200);
                response.type("application/json");
                return new Gson().toJson(product.getInStockQuantity());
            } else {
                response.status(404);
                return "Product not found";
            }
        } catch (SQLException e) {
            response.status(500);
            return e.getMessage();
        } catch (Exception e) {
            response.status(400);
            return e.getMessage();
        }
    }

    public static String addBuyer(Request request, Response response) {
        try {
            Buyer buyer = new Gson().fromJson(request.body(), Buyer.class);
            DBUtil.addBuyer(buyer);
            response.status(200);
            response.type("application/json");
            return buyerToJson(buyer);
        } catch (SQLException e) {
            response.status(500);
            return e.getMessage();
        } catch (Exception e) {
            response.status(400);
            return e.getMessage();
        }
    }

    public static String placeOrder(Request request, Response response) {
        try {
            String productId = request.params(":productId");
            int buyerId = Integer.parseInt(request.params(":buyerId"));
            OrderRequest orderRequest = new Gson().fromJson(request.body(), OrderRequest.class);
            Order order = new Order(productId, buyerId, orderRequest.getQuantity());
            Exception exception = DBUtil.placeOrder(order);
            if (exception != null) {
                throw new OrderPlacementException(exception.getMessage());
            } else {
                response.status(200);
                response.type("application/json");
                return "Order placed successfully.";
            }
        } catch (SQLException e) {
            response.status(500);
            return e.getMessage();
        } catch (Exception e) {
            response.status(400);
            return e.getMessage();
        }
    }

    public static String getBuyersAndTheirProducts(Request request, Response response) {
        try {
            Map<String, List<BuyerOrdersResponse>> buyersAndTheirProducts = DBUtil.getBuyersAndTheirProducts();
            response.status(200);
            response.type("application/json");
            return new Gson().toJson(buyersAndTheirProducts);
        } catch (SQLException e) {
            response.status(500);
            return e.getMessage();
        } catch (Exception e) {
            response.status(400);
            return e.getMessage();
        }
    }

    public static void main(String[] args) {
        port(8080);
        put("/api/v1/product", InventoryController::addProduct);
        get("/api/v1/product/:productId/stockInHand", InventoryController::getStockInHand);
        put("/api/v1/buyer", InventoryController::addBuyer);
        post("/api/v1/product/:productId/buyer/:buyerId/order", InventoryController::placeOrder);
        get("/api/v1/product/order", InventoryController::getBuyersAndTheirProducts);
    }
}