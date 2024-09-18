package com.HTShop.message_service.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicRequest {
    private String userid;
    private String password;
}

