package com.example.oopproject.model;

public class Bread extends Product {
    private String type;

    public Bread(String productId, String name, double price, String category, String allergens, int stockQuantity, Boolean isAvailable, String type) {
        super(productId, name, price, category, allergens, stockQuantity, isAvailable);
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return super.toString() + ", Type: " + this.getType();
    }
}
