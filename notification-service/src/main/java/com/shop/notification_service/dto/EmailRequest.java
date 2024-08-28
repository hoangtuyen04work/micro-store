package com.shop.notification_service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private EmailObject sender;
    private String subject;
    private String htmlContent;
    private List<EmailObject> to;
}
