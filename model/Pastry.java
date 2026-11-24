package com.example.oopproject.model;

public class Pastry extends Product {
    private String type;
    private String filling;

    public Pastry(String productId, String name, double price, String category, String allergens, int stockQuantity, Boolean isAvailable,
                  String type, String filling) {
        super(productId, name, price, category, allergens, stockQuantity, isAvailable);
        this.type = type;
        this.filling = filling;
    }

    public String getType() {
        return this.type;
    }
    public String getFilling() {
        return this.filling;
    }

    @Override
    public String toString() {
        return super.toString() + ", Type: " + this.getType() + ", Filling: " + this.getFilling();
    }
}
