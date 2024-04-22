package com.test.service;

import com.test.request.AddUserRequest;
import com.test.request.DisconnectUserRequest;
import com.test.response.AddUserResponse;
import com.test.response.DisconnectUserResponse;
import com.test.response.ListUsersResponse;

public interface UserService {

    AddUserResponse addUser(AddUserRequest request);

    DisconnectUserResponse disconnectUser(DisconnectUserRequest request);

    ListUsersResponse findConnectedUsers();
}
