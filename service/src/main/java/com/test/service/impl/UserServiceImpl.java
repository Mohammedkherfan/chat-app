package com.test.service.impl;

import com.test.bo.UserBo;
import com.test.dto.UserDto;
import com.test.enums.Status;
import com.test.exception.ChatAppException;
import com.test.repository.UserRepository;
import com.test.request.AddUserRequest;
import com.test.request.DisconnectUserRequest;
import com.test.request.LoginUserRequest;
import com.test.response.AddUserResponse;
import com.test.response.DisconnectUserResponse;
import com.test.response.ListUsersResponse;
import com.test.response.LoginUserResponse;
import com.test.service.UserService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
        UserBo userBo = userRepository.save(UserBo.builder()
                .nickName(request.getNickName())
                .fullName(request.getFullName())
                .status(request.getStatus())
                .password(request.getPassword())
                .build());
        return AddUserResponse.builder()
                .nickName(userBo.getNickName())
                .fullName(userBo.getFullName())
                .status(userBo.getStatus())
                .build();
    }

    @Override
    public DisconnectUserResponse disconnectUser(DisconnectUserRequest request) {
        Optional<UserBo> userBo = userRepository.findByNickName(request.getNickName());
        if (userBo.isEmpty())
            throw new ChatAppException("Invalid nickName, Nickname not exist");
        userBo.get().setStatus(request.getStatus());
        userRepository.save(userBo.get());
        return DisconnectUserResponse.builder()
                .nickName(userBo.get().getNickName())
                .fullName(userBo.get().getFullName())
                .status(userBo.get().getStatus())
                .build();
    }

    @Override
    public ListUsersResponse findConnectedUsers() {
        List<UserDto> users = userRepository.findAllByStatus(Status.ONLINE).stream().map(bo -> UserDto.builder()
                .nickName(bo.getNickName())
                .fullName(bo.getFullName())
                .status(bo.getStatus())
                .build())
                .toList();
        return ListUsersResponse.builder()
                .users(users)
                .build();
    }

    @Override
    public LoginUserResponse login(LoginUserRequest request) {
        String[] decoded = decode(request.getToken());
        Optional<UserBo> userBo = userRepository.findByNickName(decoded[0]);
        if (userBo.isEmpty())
            throw new ChatAppException("UnAuthorized");
        if (decoded[1].equals(userBo.get().getPassword()))
            return LoginUserResponse.builder()
                    .nickName(userBo.get().getNickName())
                    .fullName(userBo.get().getFullName())
                    .status(userBo.get().getStatus())
                    .build();
        throw new ChatAppException("UnAuthorized");
    }

    private String[] decode(String userToken) {
        if (userToken != null && userToken.toLowerCase().startsWith("basic")) {
            String base64Credentials = userToken.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            return credentials.split(":", 2);
        }
        throw new ChatAppException("Invalid user token");
    }
}
