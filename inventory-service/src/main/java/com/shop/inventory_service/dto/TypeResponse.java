package com.shop.inventory_service.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TypeResponse {
    private String id;
    private String type;
}
