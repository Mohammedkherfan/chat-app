package com.test.controller;

import com.test.dto.UserDto;
import com.test.request.AddUserRequest;
import com.test.request.DisconnectUserRequest;
import com.test.request.LoginUserRequest;
import com.test.response.AddUserResponse;
import com.test.response.DisconnectUserResponse;
import com.test.response.LoginUserResponse;
import com.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Sign up users
     * @param request
     * @return
     */
    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public AddUserResponse addUser(@Payload AddUserRequest request) {
        return userService.addUser(request);
    }

    /**
     * Sign in users
     * @param request
     * @return
     */
    @MessageMapping("/user.login")
    @SendTo("/user/login/public")
    public LoginUserResponse assUserToSocket(@Payload LoginUserRequest request) {
        return userService.assUserToSocket(request);
    }

    /**
     * Sign in users
     * @param request
     * @return
     */
    @PostMapping(value = "/user/signin")
    public ResponseEntity<LoginUserResponse> login(@RequestBody LoginUserRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    /**
     * Sign out user
     * @param request
     * @return
     */
    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public DisconnectUserResponse disconnectUser(@Payload DisconnectUserRequest request) {
        return userService.disconnectUser(request);
    }

    /**
     * List online users
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers().getUsers());
    }
}
