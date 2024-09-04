package com.shop.product_service.repository;

import com.shop.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {
    void deleteByCode(String code);
    List<Product> findByNameContaining(String name);
    @Query("SELECT p FROM Product p WHERE (:name IS NULL OR p.name LIKE %:name%) AND (:code IS NULL OR p.code LIKE %:code%)")
    Page<Product> find(@Param("name") String name, @Param("code") String code, Pageable pageable);

}
