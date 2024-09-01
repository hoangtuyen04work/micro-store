package com.shop.inventory_service.collection;


import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "inventory")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    @Id
    private String id;
    private String name;
    private String description;
    private List<String> category;
    private Long quantity;
    private String productId;
    private LocalDate importDate;
    private LocalDate expiryDate;
    private LocalDate productDate;
    private String state;
    private Long totalCost;
    private Long costPerProduct;
    private String code;
}
