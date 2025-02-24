package com.HTShop.message_service.collection;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "chat")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AChat {
    @Id
    private String chatId;
    private String user1Id;
    private String user2Id;
    private List<Message> messages;
    private LocalDateTime chatAt;
}
