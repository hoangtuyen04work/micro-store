package com.shop.product_service.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(ProductListener.class)
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String code;
    private String name;
    private Long price;
    private String description;
    private LocalDate  productionDate;
    private LocalDate expirationDate;
    private String urlImg;
    @JoinTable(
            name = "product_type",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ManyToMany()
    List<Type> types;
    @CreatedDate
    private Timestamp addAt;
    @LastModifiedDate
    private Timestamp updated_at;
}
