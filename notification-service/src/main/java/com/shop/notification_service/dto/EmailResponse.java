package com.shop.notification_service.dto;


import jakarta.persistence.Entity;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailResponse {
    private String messageId;
}
