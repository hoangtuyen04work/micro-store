package com.shop.notification_service.controller;

import com.shop.event.SendEmailRequest;
import com.shop.notification_service.exception.AppException;
import com.shop.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaController {
    private static final Logger log = LoggerFactory.getLogger(KafkaController.class);
    @Autowired
    private NotificationService notificationService;
    @KafkaListener(topics = "auth-signup", groupId = "notify-email-welcome")
    public void consumer(SendEmailRequest request) throws AppException {
        notificationService.sendEmail(request);
    }
}
