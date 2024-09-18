package com.HTShop.message_service.service;

import com.HTShop.message_service.collection.User;
import com.HTShop.message_service.repository.UserRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class UserService {
    UserRepo userRepo;
    public void create(String userid){
        userRepo.save(User.builder()
                        .userid(userid)
                .build());
    }
    public List<String> getAllUser(String userid){
        List<User> users = userRepo.findAll();
        List<String> userids = new ArrayList<>();
        for (User user : users){
            userids.add(user.getUserid());
        }
        userids.remove(userid);
        return userids;
    }
}
