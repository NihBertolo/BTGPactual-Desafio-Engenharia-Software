package com.nicolebertolo.msproduct.services;

import com.nicolebertolo.msproduct.domain.models.Product;
import com.nicolebertolo.msproduct.exceptions.ProductNotFoundException;
import com.nicolebertolo.msproduct.grpc.server.request.ProductRequest;
import com.nicolebertolo.msproduct.repository.ProductRepository;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Product findProductById(String productId) {
        LOGGER.info("[ProductService.findProductById] - Finding product by id: " +productId);

        return this.productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product not found.")
        );
    }

    public Product createProduct(ProductRequest productRequest) {
        LOGGER.info("[ProductService.createProduct] - Creating a new product");

        val product = this.productRepository.insert(
                Product.builder()
                        .id(UUID.randomUUID().toString())
                        .name(productRequest.getName())
                        .description(productRequest.getDescription())
                        .price(productRequest.getPrice())
                        .identificationCode(productRequest.getIdentificationCode())
                        .stockInfo(productRequest.getStockInfo())
                        .creationDate(LocalDateTime.now())
                        .build()
        );

        LOGGER.info("[ProductService.createProduct] - New product created with id: " +product.getId());
        return product;
    }

    public Product updateProductById(ProductRequest productRequest, String productId) {
        LOGGER.info("[ProductService.updateProductById] - Updating product with id: " +productId);

        val product = this.productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product not found.")
        );
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setIdentificationCode(productRequest.getIdentificationCode());
        product.setStockInfo(productRequest.getStockInfo());

        return this.productRepository.save(product);
    }

    public Product handleProductStockById(String productId, Integer quantity) {
        LOGGER.info("[ProductService.handleProductStockById] - Handling product Stock.");

        val product = this.productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product not found."));
        Product updatedProduct;
        if (quantity>=0) LOGGER.info("Increasing Stock...");

        val initialQty = product.getStockInfo().getQuantity();

        if (initialQty - quantity<0) {
            LOGGER.info("Product sold off!!!");
            product.getStockInfo().setQuantity(0);
            return this.productRepository.save(product);
        }

        product.getStockInfo().setQuantity(initialQty + quantity);
        updatedProduct = this.productRepository.save(product);

        LOGGER.info("[ProductService.handleProductStockById] - Stock updated. Initial Quantity:" +initialQty
                + ", actual quantity: " +updatedProduct.getStockInfo().getQuantity());
        return updatedProduct;
    }

    public List<Product> findAll() {
        LOGGER.info("[ProductService.findAll] - Finding All Products");
        return this.productRepository.findAll();
    }

    public void deleteProductById(String productId) {
        LOGGER.info("[ProductService.deleteProductById] - Deleting Product by Id: " +productId);
        this.productRepository.deleteById(productId);
    }
}
