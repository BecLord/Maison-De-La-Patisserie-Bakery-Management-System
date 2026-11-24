package com.example.oopproject.model;

import java.io.Serializable;

public class Customer implements Serializable {
    private String customerId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;

    private Customer (Builder builder) {
        this.customerId = builder.customerId;
        this.name = builder.name;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.address = builder.address;
    }

    public Customer(String customerId, String name, String email, String phoneNumber, String address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName(){
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setCustomerId(String customerId){
        this.customerId = customerId;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static class Builder {
        private String customerId;
        private String name;
        private String email;
        private String phoneNumber;
        private String address;

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Customer build() {
            if (customerId == null || name == null) throw new IllegalStateException("Please enter Id and name");
            return new Customer(this);
        }
    }

    @Override
    public String toString() {
        return "ID: " + customerId + "\nName: " + name + "\nEmail: " + email +
                "\nPhone: " + phoneNumber + "\nAddress: " + address;
    }
}

