package com.HTShop.message_service.repository;

import com.HTShop.message_service.collection.AChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepo extends MongoRepository<AChat, String> {
    Optional<AChat> getChatByUser1IdAndUser2Id(String user1Id, String user2Id);
}
