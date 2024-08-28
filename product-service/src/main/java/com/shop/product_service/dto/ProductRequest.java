package com.shop.product_service.dto;

import com.shop.product_service.entity.Type;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String id;
    private String name;
    private String description;
    private Long price;
    private String code;
    private LocalDate productionDate;
    private List<TypeRequest> types;
    private LocalDate  expirationDate;
}
