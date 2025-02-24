package com.HTShop.message_service.controller;

import com.HTShop.message_service.dto.ChatRequest;
import com.HTShop.message_service.dto.ChatResponse;
import com.HTShop.message_service.dto.MessageRequest;
import com.HTShop.message_service.dto.MessageResponse;
import com.HTShop.message_service.service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ChatController {
    SimpMessagingTemplate simpMessagingTemplate;
    ChatService chatService;
    @MessageMapping("/send")
    @SendTo("/topic/receive")
    public void createChat(@Payload MessageRequest request){
        MessageResponse response = chatService.sendMessage(request);
        simpMessagingTemplate.convertAndSend("/topic/receive/" + request.getChatId(), response);
    }
//    @SendTo("/topic/receive")
//    public MessageResponse createChat(@Payload MessageRequest request){
//        return chatService.sendMessage(request);
//    }
    @GetMapping("/chat/{userid1}/{userid2}")
    public ChatResponse getChat(@PathVariable String userid1, @PathVariable String userid2){
        return chatService.getChat(userid1, userid2);
    }
}
