package com.shop.notification_service.service;

import com.shop.notification_service.dto.EmailObject;
import com.shop.notification_service.dto.EmailRequest;
import com.shop.notification_service.dto.EmailResponse;
import com.shop.notification_service.dto.SendEmailRequest;
import com.shop.notification_service.entity.SendEmail;
import com.shop.notification_service.exception.AppException;
import com.shop.notification_service.exception.ErrorCode;
import com.shop.notification_service.repository.SendEmailRepo;
import com.shop.notification_service.repository.httpclient.EmailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final EmailClient emailClient;
    @Autowired
    private SendEmailRepo sendEmailRepo;
    private final String apiKey;
    private final String email;
    private final String name;

    public NotificationService(EmailClient emailClient,
                               @Value("${notification.email.brevo-apikey}") String apiKey,
                               @Value("${notification.email.sender}") String email,
                               @Value("${notification.email.name}") String name) {
        this.emailClient = emailClient;
        this.apiKey = apiKey;
        this.email = email;
        this.name = name;
    }

    public EmailResponse sendEmail(SendEmailRequest request) throws AppException {
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(EmailObject.builder()
                        .name(name)
                        .email(email)
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            EmailResponse response =  emailClient.sendEmail(apiKey, emailRequest);
            SendEmail sendEmail = SendEmail.builder()
                    .htmlContent(request.getHtmlContent())
                    .subject(request.getSubject())
                    .sender(email)
                    .messageId(response.getMessageId())
                    .build();
            sendEmailRepo.save(sendEmail);
            return response;
        } catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
