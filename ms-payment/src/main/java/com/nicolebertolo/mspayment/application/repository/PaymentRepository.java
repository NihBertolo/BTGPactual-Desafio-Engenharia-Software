package com.nicolebertolo.mspayment.application.repository;

import com.nicolebertolo.mspayment.application.domain.models.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
}
