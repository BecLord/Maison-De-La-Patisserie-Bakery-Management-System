package com.example.oopproject.model;

public class Cake extends Product {
    private String flavour;
    private int size;
    private Boolean customisable;

    public Cake(String productId, String name, double price, String category, String allergens, int stockQuantity, Boolean isAvailable,
                String flavour, int size, Boolean customisable) {
        super(productId, name, price, category, allergens, stockQuantity, isAvailable);
        this.flavour = flavour;
        this.size = size;
        this.customisable = customisable;
    }

    public String getFlavour() {
        return this.flavour;
    }

    public int getSize(){
        return this.size;
    }

    public Boolean getCustomisable() {
        return this.customisable;
    }

    @Override
    public String toString() {
        return super.toString() + ", Flavour: " + this.getFlavour() + ", Size: " + this.getSize() + ", Customised? - " + this.getCustomisable();
    }
}
