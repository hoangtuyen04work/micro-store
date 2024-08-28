package com.shop.product_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String type;
    @ManyToMany(mappedBy = "types", cascade = CascadeType.REMOVE)
    List<Product> products;
}
