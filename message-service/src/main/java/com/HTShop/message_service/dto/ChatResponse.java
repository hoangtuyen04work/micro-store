package com.HTShop.message_service.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String chatId;
    private String user1Id;
    private String user2Id;
    private List<MessageResponse> messageResponses;
}
