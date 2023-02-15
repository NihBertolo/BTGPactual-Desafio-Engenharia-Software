package com.nicolebertolo.mscustomer.repository;

import com.nicolebertolo.mscustomer.domain.models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
