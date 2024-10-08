package com.shop.event;

import com.shop.notification_service.dto.EmailObject;
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
