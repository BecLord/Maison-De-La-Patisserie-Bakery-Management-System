package com.example.oopproject.model;

import java.util.ArrayList;
import java.io.Serializable;

public class Order implements Serializable {
    private Customer customer;
    private ArrayList<Product> products;
    private ArrayList<Integer> quantities;
    private double totalPrice;

    public Order(Customer customer) {
        this.customer = customer;
        this.products = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void addProduct(Product product) {
        int index = products.indexOf(product);
        if (index != -1) {
            quantities.set(index, quantities.get(index) + 1);
        } else {
            products.add(product);
            quantities.add(1);
        }
        totalPrice += product.getPrice();
        product.decreasesStock(1);
    }

    public double getTotalPrice() { return totalPrice; }
    public ArrayList<Product> getProducts() { return products; }
    public ArrayList<Integer> getQuantities() { return quantities; }

    @Override
    public String toString() {
        StringBuilder summary = new StringBuilder();
        summary.append("Order for: ").append(customer.getName()).append("\n");
        for (int i = 0; i < products.size(); i++) {
            summary.append("Product: ").append(products.get(i).getName())
                    .append(" x").append(quantities.get(i))
                    .append(" - €").append(products.get(i).getPrice()).append("\n");
        }
        summary.append("Total: €").append(totalPrice).append("\n");
        return summary.toString();
    }
}
