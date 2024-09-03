package com.shop.notification_service.controller;

import com.shop.notification_service.dto.ApiResponse;
import com.shop.notification_service.dto.EmailResponse;
import com.shop.event.SendEmailRequest;
import com.shop.notification_service.exception.AppException;
import com.shop.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendEmailController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/internal/notify/welcome")
    public ApiResponse<EmailResponse> notifyWelcome(@RequestBody SendEmailRequest request) throws AppException {
        System.err.println("SendEmailController: notifyWelcome");
        return ApiResponse.<EmailResponse>builder()
                .data(notificationService.sendEmail(request))
                .build();
    }
}
