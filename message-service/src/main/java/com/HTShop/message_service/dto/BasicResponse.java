package com.HTShop.message_service.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicResponse {
    private String userid;
    private boolean ok;
}

