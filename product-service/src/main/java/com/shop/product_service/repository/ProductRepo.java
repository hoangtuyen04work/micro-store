package com.shop.product_service.repository;

import com.shop.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {
    void deleteByCode(String code);
    List<Product> findByNameContaining(String name);
}
