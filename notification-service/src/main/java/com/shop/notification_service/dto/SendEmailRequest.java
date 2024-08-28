package com.shop.notification_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {
    private EmailObject to;
    private String subject;
    private String htmlContent;
}
