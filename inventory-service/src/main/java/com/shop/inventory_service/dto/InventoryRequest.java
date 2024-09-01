package com.shop.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    private String id;
    private String name;
    private String productId;
    private String description;
    private List<String> category;
    private Long quantity;
    private String code;
    private LocalDate importDate;
    private LocalDate expiryDate;
    private LocalDate productDate;
    private String state;
    private Long totalCost;
    private Long costPerProduct;
}
