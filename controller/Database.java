package com.example.oopproject.controller;

import com.example.oopproject.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Database instance;
    private Connection connection;

    private String url = "jdbc:mysql://127.0.0.1:3306/bakery";
    private String user = "root";
    private String password = "Dairy7purple!";

    private Database() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    public static Database getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void insertCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO Customer (name, email, phone, address) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhoneNumber());
            stmt.setString(4, customer.getAddress());
            stmt.executeUpdate();
        }
    }

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT customer_id, name, email, phone, address FROM Customer";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new Customer.Builder()
                        .customerId(rs.getString("customer_id"))
                        .name(rs.getString("name"))
                        .email(rs.getString("email"))
                        .phoneNumber(rs.getString("phone"))
                        .address(rs.getString("address"))
                        .build());
            }
        }
        return customers;
    }
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            System.err.println("Error closing DB connection: " + e.getMessage());
        }
    }
}