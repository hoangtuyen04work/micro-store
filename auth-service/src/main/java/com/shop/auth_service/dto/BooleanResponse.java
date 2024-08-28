package com.shop.auth_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BooleanResponse {
    private boolean isValid;
}
