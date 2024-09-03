package com.shop.auth_service.repository.httpclient;

import com.shop.auth_service.configuration.AuthenticationRequestInterceptor;
import com.shop.auth_service.dto.ApiResponse;
import com.shop.auth_service.dto.AuthResponse;
import com.shop.event.SendEmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${app.service.notification.name}", url = "${app.service.notification.url}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface NotificationClient {
    @PostMapping(value = "/internal/notify/welcome", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<AuthResponse> emailWelcome(@RequestBody SendEmailRequest request);
}

