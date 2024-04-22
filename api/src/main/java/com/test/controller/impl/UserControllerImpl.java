package com.test.controller.impl;

import com.test.controller.UserController;
import com.test.request.AddUserRequest;
import com.test.request.DisconnectUserRequest;
import com.test.response.AddUserResponse;
import com.test.response.DisconnectUserResponse;
import com.test.response.ListUsersResponse;
import com.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Autowired
    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public AddUserResponse addUser(@Payload AddUserRequest request) {
        return userService.addUser(request);
    }

    @Override
    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/topic")
    public DisconnectUserResponse disconnectUser(@Payload DisconnectUserRequest request) {
        return userService.disconnectUser(request);
    }

    @Override
    @GetMapping("/users")
    public ListUsersResponse findConnectedUsers() {
        return userService.findConnectedUsers();
    }
}
