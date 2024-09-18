package com.HTShop.message_service.collection;

import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String id = UUID.randomUUID().toString();
    private String content;
    private LocalDateTime time;
    private String senderid;
    private String recipientid;
}
