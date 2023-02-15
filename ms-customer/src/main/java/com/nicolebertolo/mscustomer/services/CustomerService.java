package com.nicolebertolo.mscustomer.services;

import com.nicolebertolo.mscustomer.domain.models.Customer;
import com.nicolebertolo.mscustomer.grpc.server.request.CustomerRequest;
import com.nicolebertolo.mscustomer.repository.CustomerRepository;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Customer findCustomerById(String customerId) {
        LOGGER.info("[CustomerService.findCustomerById] - Finding customer by id: " + customerId);
        val customer = this.customerRepository.findById(customerId);

        return customer.orElse(null);
    }

    public Customer createCustomer(CustomerRequest customerRequest) {
        LOGGER.info("[CustomerService.createCustomer] - Creating a new customer");
        val customer = this.customerRepository.insert(
                Customer.builder()
                        .id(UUID.randomUUID().toString())
                        .name(customerRequest.getName())
                        .lastname(customerRequest.getLastname())
                        .documents(customerRequest.getDocuments())
                        .addresses(customerRequest.getAddresses())
                        .build()
        );
        LOGGER.info("[CustomerService.createCustomer] - A new customer with id: " + customer.getId() + " has been created");
        return customer;
    }

    public Customer updateCustomerById(CustomerRequest customerRequest, String customerId) {
        LOGGER.info("[CustomerService.updateCustomerById] - Updating customer by id:" + customerId);
        val customer = this.customerRepository.findById(customerId);

        if (customer.isPresent()) {
            val updateCustomer = customer.get();
            updateCustomer.setName(customerRequest.getName());
            updateCustomer.setLastname(customerRequest.getLastname());
            updateCustomer.setDocuments(customerRequest.getDocuments());
            updateCustomer.setAddresses(customerRequest.getAddresses());

            LOGGER.info("[CustomerService.updateCustomerById] - Updated customer by id:" + customerId);
            return this.customerRepository.save(updateCustomer);
        } else {
            LOGGER.info("[CustomerService.updateCustomerById] - Customer not found.");
            return null;
        }
    }

    public void deleteCustomerById(String customerId) {
        LOGGER.info("[CustomerService.deleteCustomerById] - Deleting customer by id:" + customerId);

        this.customerRepository.deleteById(customerId);
    }

    public List<Customer> findAll() {
        LOGGER.info("[CustomerService.findAll] - Finding all customers");

        val customers = this.customerRepository.findAll();
        LOGGER.info("[CustomerService.findAll] - " + customers.size() + " were found");
        return customers;
    }
}
