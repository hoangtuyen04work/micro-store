package com.shop.notification_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailObject {
    private String name;
    private String email;
}
