package com.shop.inventory_service.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private String id;
    private String code;
    private String productId;
    private String name;
    private String description;
    private List<String> category;
    private Long quantity;
    private LocalDate importDate;
    private LocalDate expiryDate;
    private LocalDate productDate;
    private String state;
    private Long totalCost;
    private Long costPerProduct;
}
