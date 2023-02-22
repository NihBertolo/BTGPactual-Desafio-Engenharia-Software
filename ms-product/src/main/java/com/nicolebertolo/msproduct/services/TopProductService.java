package com.nicolebertolo.msproduct.services;

import com.nicolebertolo.msproduct.domain.models.TopProductSeller;
import com.nicolebertolo.msproduct.grpc.client.OrderServiceGRPC;
import com.nicolebertolo.msproduct.grpc.client.response.OrderResponse;
import com.nicolebertolo.msproduct.repository.TopProductSellerRepository;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class TopProductService {

    @Autowired
    private TopProductSellerRepository topProductSellerRepository;

    @Autowired
    private OrderServiceGRPC orderServiceGRPC;

    @Autowired
    private ProductService productService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public List<TopProductSeller> saveCache(String tracing) {
        LOGGER.info("[TopProductService.saveCache] - Start , tracing: " +tracing);
        val ordersGRPC = this.orderServiceGRPC.findAllOrders(tracing)
                .getOrderDtoList().stream().map(OrderResponse::toResponse).collect(Collectors.toList());

        List<String> productsIds = new ArrayList<>();

        LOGGER.info("[TopProductService.saveCache] - Collecting productsIds. . .");
        ordersGRPC.forEach(order -> order.getDetails().getItems().forEach( item -> {
            productsIds.add(item.getProductId());
        }));

        Map<String, Long> countMap = productsIds.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Map.Entry<String, Long>> sortedList = new ArrayList<>(countMap.entrySet());

        sortedList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        List<TopProductSeller> topProducts = new ArrayList<>();

        if (sortedList.isEmpty()) {
            LOGGER.info("[TopProductService.saveCache] - Not sold products found.");
            return topProducts;
        }

        for (int i=0; i<=5; i++) {
            val product = this.productService.findProductById(sortedList.get(i).getKey());

            val topProduct = TopProductSeller.builder()
                    .id(UUID.randomUUID().toString())
                    .product(product)
                    .quantitySold(sortedList.get(i).getValue())
                    .build();


            topProducts.add(topProduct);
        }
        LOGGER.info("[TopProductService.saveCache] - End");
        return (List<TopProductSeller>) this.topProductSellerRepository.saveAll(topProducts);
    }

    public List<TopProductSeller> getCache(String tracing) {
        LOGGER.info("[TopProductService.getCache] - Start , tracing: " +tracing);
        return (List<TopProductSeller>) this.topProductSellerRepository.findAll();
    }
}
