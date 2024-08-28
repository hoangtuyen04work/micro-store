package com.shop.auth_service.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailObject {
    private String name;
    private String email;
}
