package com.hotel.controller;

import com.hotel.model.Customer;
import com.hotel.storage.FileStorage;
import java.util.List;

public class CustomerController {
    private List<Customer> customers;
    private int nextId = 1;

    public CustomerController() {
        customers = FileStorage.loadCustomers();
        if (!customers.isEmpty())
            nextId = customers.stream()
                .mapToInt(Customer::getCustomerId).max().getAsInt() + 1;
    }

    public void addCustomer(String name, String contact, boolean loyalty) {
        customers.add(new Customer(nextId++, name, contact, loyalty));
        FileStorage.saveCustomers(customers);
    }

    public List<Customer> getAllCustomers() { return customers; }

    public Customer findCustomer(int id) {
        return customers.stream()
            .filter(c -> c.getCustomerId() == id)
            .findFirst().orElse(null);
    }

    public void save() { FileStorage.saveCustomers(customers); }
}