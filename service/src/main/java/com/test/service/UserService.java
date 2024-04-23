package com.test.service;

import com.test.request.AddUserRequest;
import com.test.request.DisconnectUserRequest;
import com.test.request.LoginUserRequest;
import com.test.response.AddUserResponse;
import com.test.response.DisconnectUserResponse;
import com.test.response.ListUsersResponse;
import com.test.response.LoginUserResponse;

public interface UserService {

    AddUserResponse addUser(AddUserRequest request);

    DisconnectUserResponse disconnectUser(DisconnectUserRequest request);

    ListUsersResponse findConnectedUsers();

    LoginUserResponse login(LoginUserRequest request);
}
