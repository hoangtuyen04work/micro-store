package com.shop.product_service.entity;

import com.shop.product_service.service.ProductRedisService;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ProductListener {
    private static final Logger log = LoggerFactory.getLogger(ProductListener.class);
    ProductRedisService productRedisService;

    @PrePersist
    public void prePersist(Product product) {
        log.info("Pre persist product: {}", product);
    }
    @PostPersist
    public void postPersist(Product product) {
        log.info("Post persist product: {}", product);
        productRedisService.clear();
    }
    @PreUpdate
    public void preUpdate(Product product) {
        log.info("Pre update product: {}", product);
    }
    @PostUpdate
    public void postUpdate(Product product) {
        log.info("Post update product: {}", product);
        productRedisService.clear();
    }
    @PreRemove
    public void preRemove(Product product) {
        log.info("Pre delete product: {}", product);
    }
    @PostRemove
    public void postRemove(Product product){
        log.info("Post remove product: {}", product);
        productRedisService.clear();
    }

}
