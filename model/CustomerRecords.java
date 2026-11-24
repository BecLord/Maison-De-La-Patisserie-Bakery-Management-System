package com.example.oopproject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerRecords implements Serializable {
    private List<Customer> customers;

    public CustomerRecords() {
        customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public boolean removeCustomerById(String customerId) {
        return customers.removeIf(c -> c.getCustomerId().equals(customerId));
    }

    public Customer findCustomerById(String customerId) {
        for (Customer c : customers) {
            if (c.getCustomerId().equals(customerId)) {
                return c;
            }
        }
        return null;
    }

    public List<Customer> getAllCustomers() {
        return customers;
    }

    public List<Customer> findCustomersByName(String name) {
        List<Customer> result = new ArrayList<>();
        for (Customer c: customers) {
            if (c.getName().equalsIgnoreCase(name)) {
                result.add(c);
            }
        }
        return result;
    }
}
