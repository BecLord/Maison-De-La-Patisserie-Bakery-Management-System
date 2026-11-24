package com.example.oopproject.model;

import java.io.Serializable;

public abstract class Product implements Serializable {
    private String productId;
    private String name;
    private double price;
    private String category;
    private String allergens;
    private int stockQuantity;
    private Boolean isAvailable;

    public Product(String productId, String name, double price, String category, String allergens, int stockQuantity, Boolean isAvailable) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.allergens = allergens;
        this.stockQuantity = stockQuantity;
        this.isAvailable = isAvailable;

    }

    public String getProductId(){
        return productId;
    }

    public String getName(){
        return name;
    }

    public double getPrice(){
        return price;
    }

    public String getCategory(){
        return category;
    }

    public String getAllergens(){
        return allergens;
    }

    public int getStockQuantity(){
        return stockQuantity;
    }

    public Boolean getIsAvailable(){
        return isAvailable;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setIsAvailable(Boolean isAvailable){
        this.isAvailable = isAvailable;
    }

    public void decreasesStock(int quantity) {
        if (quantity > 0 && stockQuantity >= quantity) {
            this.stockQuantity -= quantity;
        } else {
            System.out.println("Insufficient stock or invalid quantity.");
        }
    }

    @Override
    public String toString() {
        return name + "- €" + price + " - " + category;
    }
}