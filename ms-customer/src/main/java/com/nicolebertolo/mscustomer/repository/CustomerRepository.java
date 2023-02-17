package com.nicolebertolo.mscustomer.repository;

import com.nicolebertolo.mscustomer.domain.models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
}
