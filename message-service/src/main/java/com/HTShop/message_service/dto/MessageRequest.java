package com.HTShop.message_service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private String chatId;
    private String content;
    private String senderid;
    private String recipientid;
    private LocalDateTime time;
}
