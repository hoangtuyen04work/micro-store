package com.shop.auth_service.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private EmailObject to;
    private String htmlContent;
    private String subject;
}
