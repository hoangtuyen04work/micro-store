package com.shop.product_service.repository;

import com.shop.product_service.entity.Type;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepo extends JpaRepository<Type, String> {
    void deleteByType(String type);
    @Query("select count(t) > 0 from Type t where t.type = :type")
    boolean existsByType(@Param("type") String type);
    @Query("select t from Type t where t.type = :type")
    Type findByTypeName(String type);
}
