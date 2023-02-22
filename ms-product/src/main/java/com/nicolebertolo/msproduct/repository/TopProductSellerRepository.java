package com.nicolebertolo.msproduct.repository;

import com.nicolebertolo.msproduct.domain.models.TopProductSeller;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopProductSellerRepository extends CrudRepository<TopProductSeller, String> {
}
