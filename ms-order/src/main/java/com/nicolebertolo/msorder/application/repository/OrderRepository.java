package com.nicolebertolo.msorder.application.repository;

import com.nicolebertolo.msorder.application.domain.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}
