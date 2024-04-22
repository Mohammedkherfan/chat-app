package com.test.controller;

import com.test.request.AddUserRequest;
import com.test.request.DisconnectUserRequest;
import com.test.response.AddUserResponse;
import com.test.response.DisconnectUserResponse;
import com.test.response.ListUsersResponse;
import com.test.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public AddUserResponse addUser(@Payload @Valid AddUserRequest request) {
        return userService.addUser(request);
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/topic")
    public DisconnectUserResponse disconnectUser(@Payload @Valid DisconnectUserRequest request) {
        return userService.disconnectUser(request);
    }

    @GetMapping("/users")
    public ListUsersResponse findConnectedUsers() {
        return userService.findConnectedUsers();
    }
}
