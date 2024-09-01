package com.shop.inventory_service.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TypeRequest {
    private String id;
    private String type;
}
