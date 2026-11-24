package com.example.oopproject.controller;

import com.example.oopproject.model.Customer;
import com.example.oopproject.model.Order;

public class Payment {

    public enum PaymentMethod {
        CASH, CARD, ONLINE
    }

    private Customer customer;
    private Order order;
    private PaymentMethod method;
    private boolean paymentComplete;

    public Payment(Customer customer, Order order) {
        this.customer = customer;
        this.order = order;
        this.paymentComplete = false;
    }

    public void setPaymentMethod(PaymentMethod method) {
        this.method = method;
    }

    public PaymentMethod getPaymentMethod() {
        return method;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Order getOrder() {
        return order;
    }

    public boolean makePayment() {
        if (order == null || method == null) {
            System.out.println("Order or payment method not set.");
            paymentComplete = false;
            return false;
        }
        System.out.println("Processing payment for " + customer.getName());
        System.out.println("Order total: €" + order.getTotalPrice());
        System.out.println("Payment method: " + method);
        paymentComplete = true;
        System.out.println("Payment successful!");
        return true;
    }

    public boolean isPaymentComplete() {
        return paymentComplete;
    }

    public String getPaymentSummary() {
        return "Customer: " + customer.getName() + "\n"
                + "Order total: €" + order.getTotalPrice() + "\n"
                + "Method: " + (method != null ? method : "Not selected") + "\n"
                + "Status: " + (paymentComplete ? "Paid" : "Pending");
    }
}

