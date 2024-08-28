package com.shop.product_service.repository;

import com.shop.product_service.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepo extends JpaRepository<Type, String> {
    void deleteByType(String type);
}
