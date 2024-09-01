package com.shop.inventory_service.dto;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private Long price;
    private String code;
    private LocalDate productionDate;
    private Timestamp addAt;
    private LocalDate  expirationDate;
    private List<TypeResponse> types = new ArrayList<>();
}
