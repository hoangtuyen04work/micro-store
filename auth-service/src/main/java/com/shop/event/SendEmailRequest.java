package com.shop.event;

import com.shop.auth_service.dto.EmailObject;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest {
    private EmailObject to;
    private String htmlContent;
    private String subject;
}
