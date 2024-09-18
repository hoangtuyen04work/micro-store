package com.HTShop.message_service.service;

import com.HTShop.message_service.collection.AChat;
import com.HTShop.message_service.collection.Message;
import com.HTShop.message_service.dto.ChatRequest;
import com.HTShop.message_service.dto.ChatResponse;
import com.HTShop.message_service.dto.MessageRequest;
import com.HTShop.message_service.dto.MessageResponse;
import com.HTShop.message_service.repository.ChatRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.codecs.jsr310.LocalDateTimeCodec;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ChatService {
    ChatRepo chatRepo;
    public List<String> getAllUser(){
        List<AChat> chats = chatRepo.findAll();
        List<String> ids = new ArrayList<>();
        for(AChat aChat : chats){
            ids.add(aChat.getUser1Id());
        }
        return ids;
    }
    public ChatResponse createAChat(ChatRequest request){
        AChat aChat = chatRepo.save(AChat.builder()
                        .user1Id(request.getUser1Id())
                        .user2Id(request.getUser2Id())
                .build());
        return ChatResponse.builder()
                .user1Id(request.getUser1Id())
                .user2Id(request.getUser2Id())
                .id(aChat.getId())
                .build();
    }

    public MessageResponse sendMessage(MessageRequest messageRequest) {
        AChat achat = chatRepo.findById(messageRequest.getChatId()).orElse(null);
        Message message = Message.builder()
                .content(messageRequest.getContent())
                .recipientid(messageRequest.getRecipientid()) // Người nhận tin nhắn
                .senderid(messageRequest.getSenderid()) // Người gửi tin nhắn
                .time(LocalDateTime.now()) // Sử dụng thời gian hiện tại
                .build();
        if (achat != null) {
            List<Message> messages = achat.getMessages();
            // Kiểm tra nếu danh sách tin nhắn rỗng, khởi tạo mới
            if (messages != null) {
                messages.add(message); // Thêm tin nhắn vào danh sách (không dùng addFirst)
            } else {
                messages = new ArrayList<>();
                messages.add(message);
                achat.setMessages(messages);
            }

            // Lưu lại bản cập nhật cuộc trò chuyện
            chatRepo.save(achat);
        }
        // Chuyển đổi tin nhắn thành dạng phản hồi
        return toMessageResponse(message);
    }

    public ChatResponse getChat(String user1Id, String user2Id){
        AChat aChat;
        if(chatRepo.getChatByUser1IdAndUser2Id(user1Id, user2Id).isPresent()){
            aChat = chatRepo.getChatByUser1IdAndUser2Id(user1Id, user2Id).get();
        }
        else if(chatRepo.getChatByUser1IdAndUser2Id(user2Id, user1Id).isPresent()){
            aChat = chatRepo.getChatByUser1IdAndUser2Id(user2Id, user1Id).get();
        }
        else{
            aChat = chatRepo.save(AChat.builder()
                    .user1Id(user1Id)
                    .user2Id(user2Id)
                    .build());
        }
        List<MessageResponse> responses  = toListMessageResponse(aChat.getMessages());
        return  ChatResponse.builder()
                .id(aChat.getId())
                .user1Id(aChat.getUser1Id())
                .user2Id(aChat.getUser2Id())
                .messageResponses(responses)
                .build();
    }
    private MessageResponse toMessageResponse(Message messages){
        return MessageResponse.builder()
                .content(messages.getContent())
                .time(messages.getTime())
                .id(messages.getId())
                .senderid(messages.getSenderid())
                .recipientid(messages.getRecipientid())
                .build();
    }
    private List<MessageResponse> toListMessageResponse(List<Message> messages){
        if(messages != null) {
            List<MessageResponse> messageResponses = new ArrayList<>();
            for (Message message : messages) {
                messageResponses.add(MessageResponse.builder()
                                .senderid(message.getSenderid())
                                .recipientid(message.getRecipientid())
                        .id(message.getId())
                        .content(message.getContent())
                        .time(message.getTime())
                        .build());
            }
            return messageResponses;
        }
        return null;
    }
}
