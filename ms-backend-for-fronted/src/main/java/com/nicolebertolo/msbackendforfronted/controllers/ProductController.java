package com.nicolebertolo.msbackendforfronted.controllers;

import com.nicolebertolo.msbackendforfronted.grpc.client.domain.product.ProductRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.product.ProductResponse;
import com.nicolebertolo.msbackendforfronted.services.ProductServiceAPI;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    @Autowired
    private ProductServiceAPI productServiceAPI;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable("id") String productId) {
        LOGGER.info("[ProductController.findProductId] - Controller Request");
        val tracing = UUID.randomUUID().toString();

        return ResponseEntity.ok(this.productServiceAPI.findProductById(productId, tracing));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        LOGGER.info("[ProductController.createProduct] - Controller Request");
        val tracing = UUID.randomUUID().toString();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.productServiceAPI.createProduct(productRequest, tracing));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAllProducts() {
        LOGGER.info("[ProductController.findAllProducts] - Controller Request");
        val tracing = UUID.randomUUID().toString();

        return ResponseEntity.status(HttpStatus.OK).body(this.productServiceAPI.findAllProducts(tracing));
    }
}
