package com.example.oopproject.controller;

import com.example.oopproject.model.*;
import java.util.ArrayList;

public class Checkout {
    private ArrayList<Customer> customers;
    private ArrayList<Product> availableProducts;
    private ArrayList<Order> orders = new ArrayList<>();
    private Order currentOrder = null;

    public Checkout(ArrayList<Customer> customers, ArrayList<Product> products) {
        this.customers = customers;
        this.availableProducts = products;
    }

    public String startOrder(String customerName) {
        for (Customer customer : customers) {
            if (customer.getName().equalsIgnoreCase(customerName)) {
                currentOrder = new Order(customer);
                return "Order started for " + customerName;
            }
        }
        return "Customer not found.";
    }

    public String addProductToOrder(String productName, int quantity) {
        if (currentOrder == null) return "No order started.";
        for (Product product : availableProducts) {
            if (product.getName().equalsIgnoreCase(productName)) {
                for (int i = 0; i < quantity; i++) currentOrder.addProduct(product);
                return "Added " + quantity + " x " + productName;
            }
        }
        return "Product not found.";
    }

    public String finalizeOrder() {
        if (currentOrder == null) return "No order to finalize.";
        orders.add(currentOrder);
        String summary = currentOrder.toString();
        currentOrder = null;
        return summary;
    }

    public double getCurrentTotal() {
        return currentOrder != null ? currentOrder.getTotalPrice() : 0.0;
    }
}
