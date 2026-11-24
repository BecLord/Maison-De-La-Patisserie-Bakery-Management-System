package com.example.oopproject.controller;

import com.example.oopproject.model.*;

import java.util.ArrayList;
import java.util.List;

public class Admin {

    private ArrayList<Product> products;
    private ArrayList<Order> currentOrders;
    private ArrayList<Order> pastOrders;
    private ArrayList<Customer> customers;

    public Admin(ArrayList<Product> products, ArrayList<Order> currentOrders, ArrayList<Order> pastOrders, ArrayList<Customer> customers) {
        this.products = products;
        this.currentOrders = currentOrders;
        this.pastOrders = pastOrders;
        this.customers = customers;
    }

    public void addProduct(Product product) {
        products.add(product);
        System.out.println("Product added: " + product.getName());
    }

    public boolean editProduct(String productId, Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductId().equals(productId)) {
                products.set(i, updatedProduct);
                System.out.println("Product updated: " + updatedProduct.getName());
                return true;
            }
        }
        System.out.println("Product not found: " + productId);
        return false;
    }

    public boolean removeProduct(String productId) {
        return products.removeIf(p -> p.getProductId().equals(productId));
    }

    public List<Order> getCurrentOrders() {
        return currentOrders;
    }

    public List<Order> getPastOrders() {
        return pastOrders;
    }

    public void completeOrder(Order order) {
        if (currentOrders.remove(order)) {
            pastOrders.add(order);
            System.out.println("Order moved to past orders.");
        }
    }

    public List<Customer> getAllCustomers() {
        return customers;
    }

    public Customer findCustomerById(String customerId) {
        for (Customer c : customers) {
            if (c.getCustomerId().equals(customerId)) {
                return c;
            }
        }
        return null;
    }
}
