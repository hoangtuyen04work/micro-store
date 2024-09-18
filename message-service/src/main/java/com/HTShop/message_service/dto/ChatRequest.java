package com.HTShop.message_service.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private String id;
    private String user1Id;
    private String user2Id;
}
