package com.example.oopproject;

import com.example.oopproject.model.*;
import com.example.oopproject.controller.Admin;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bakery implements Serializable {
    private ArrayList<Customer> customers;
    private ArrayList<Product> products;
    private ArrayList<Order> currentOrders;
    private ArrayList<Order> pastOrders;
    private Admin admin;
    private Order cartOrder;
    private Customer loggedInCustomer;

    public Bakery() {
        customers = new ArrayList<>();
        products = new ArrayList<>();
        currentOrders = new ArrayList<>();
        pastOrders = new ArrayList<>();
        admin = new Admin(products, currentOrders, pastOrders, customers);
        cartOrder = null;
        loggedInCustomer = null;
    }

    public boolean register(String customerId, String name, String email, String phoneNumber, String address) {
        for (Customer c : customers) {
            if (c.getCustomerId().equals(customerId) || c.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        Customer newCustomer = new Customer(customerId, name, email, phoneNumber, address);
        customers.add(newCustomer);
        return true;
    }

    public boolean login(String identifier) {
        for (Customer c : customers) {
            if (c.getCustomerId().equals(identifier) || c.getEmail().equalsIgnoreCase(identifier)) {
                loggedInCustomer = c;
                cartOrder = new Order(loggedInCustomer);
                return true;
            }
        }
        return false;
    }

    public List<Product> listAvailableProducts() {
        List<Product> available = new ArrayList<>();
        for (Product p : products) {
            if (p.getIsAvailable() && p.getStockQuantity() > 0) {
                available.add(p);
            }
        }
        return available;
    }

    public List<String> getAllergyInfo() {
        Allergies allergies = new Allergies();
        return allergies.getAllergies();
    }

    public boolean addToCart(Product product, int quantity) {
        if (loggedInCustomer == null || cartOrder == null) return false;
        for (int i = 0; i < quantity; i++) {
            cartOrder.addProduct(product);
        }
        return true;
    }

    public Order getCart() {
        return cartOrder;
    }

    public boolean checkoutCart() {
        if (cartOrder != null && cartOrder.getProducts().size() > 0) {
            currentOrders.add(cartOrder);
            cartOrder = new Order(loggedInCustomer);
            return true;
        }
        return false;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void logout() {
        loggedInCustomer = null;
        cartOrder = null;
    }
}