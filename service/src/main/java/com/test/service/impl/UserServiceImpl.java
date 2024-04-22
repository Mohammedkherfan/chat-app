package com.test.service.impl;

import com.test.bo.UserBo;
import com.test.dto.UserDto;
import com.test.enums.ChatStatus;
import com.test.enums.Status;
import com.test.exception.ChatAppException;
import com.test.repository.UserRepository;
import com.test.request.AddUserRequest;
import com.test.request.DisconnectUserRequest;
import com.test.response.AddUserResponse;
import com.test.response.DisconnectUserResponse;
import com.test.response.ListUsersResponse;
import com.test.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AddUserResponse addUser(AddUserRequest request) {
        Boolean isExist = userRepository.existsByUsername(request.getUsername());
        if (isExist)
            throw new ChatAppException("Username already used");
        UserBo userBo = userRepository.save(UserBo.builder()
                .username(request.getUsername())
                .fullName(request.getFullName())
                .status(Status.ACTIVE)
                .chatStatus(ChatStatus.OFFLINE)
                .build());
        return AddUserResponse.builder()
                .username(userBo.getUsername())
                .fullName(userBo.getFullName())
                .status(userBo.getStatus())
                .chatStatus(userBo.getChatStatus())
                .build();
    }

    @Override
    public DisconnectUserResponse disconnectUser(DisconnectUserRequest request) {
        Optional<UserBo> userBo = userRepository.findByUsername(request.getUsername());
        if (userBo.isEmpty())
            throw new ChatAppException("Invalid username, username not exist");
        userBo.get().setChatStatus(ChatStatus.OFFLINE);
        userRepository.save(userBo.get());
        return DisconnectUserResponse.builder()
                .username(userBo.get().getUsername())
                .fullName(userBo.get().getFullName())
                .status(userBo.get().getStatus())
                .chatStatus(userBo.get().getChatStatus())
                .build();
    }

    @Override
    public ListUsersResponse findConnectedUsers() {
        List<UserDto> users = userRepository.findAllByChatStatus(ChatStatus.ONlINE).stream().map(bo -> UserDto.builder()
                .username(bo.getUsername())
                .fullName(bo.getFullName())
                .status(bo.getStatus())
                .chatStatus(bo.getChatStatus())
                .build())
                .toList();
        return ListUsersResponse.builder()
                .users(users)
                .build();
    }
}
