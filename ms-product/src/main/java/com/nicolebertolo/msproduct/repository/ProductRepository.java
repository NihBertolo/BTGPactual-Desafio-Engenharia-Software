package com.nicolebertolo.msproduct.repository;

import com.nicolebertolo.msproduct.domain.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
}
