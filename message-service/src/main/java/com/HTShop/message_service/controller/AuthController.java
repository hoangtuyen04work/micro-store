package com.HTShop.message_service.controller;

import com.HTShop.message_service.dto.BasicRequest;
import com.HTShop.message_service.dto.BasicResponse;
import com.HTShop.message_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class AuthController {
    UserService userService;
    @PostMapping("/login")
    public BasicResponse login(@RequestBody BasicRequest request) {
        System.err.println("This is login");
        userService.create(request.getUserid());
        return BasicResponse.builder()
                .ok(true)
                .userid(request.getUserid())
                .build();
    }
    @GetMapping("/all/{userid}")
    public List<String> getAllMessager(@PathVariable String userid){
        System.err.println("This is all messager");
        return userService.getAllUser(userid);
    }
}
