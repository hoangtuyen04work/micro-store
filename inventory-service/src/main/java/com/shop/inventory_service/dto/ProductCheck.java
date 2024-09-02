package com.shop.inventory_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCheck {
    private String id;
    private Long number;
}
