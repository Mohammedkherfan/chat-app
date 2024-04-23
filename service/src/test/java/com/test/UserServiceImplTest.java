package com.test;

import com.test.bo.UserBo;
import com.test.enums.Status;
import com.test.exception.ChatAppException;
import com.test.repository.UserRepository;
import com.test.request.AddUserRequest;
import com.test.request.DisconnectUserRequest;
import com.test.request.LoginUserRequest;
import com.test.response.AddUserResponse;
import com.test.response.DisconnectUserResponse;
import com.test.response.LoginUserResponse;
import com.test.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addUser_ValidRequest_ReturnsAddUserResponse() {
        AddUserRequest request = new AddUserRequest("nickname", "fullname", Status.ONLINE, "password");
        UserBo userBo = UserBo.builder()
                .nickName(request.getNickName())
                .fullName(request.getFullName())
                .status(request.getStatus())
                .password(request.getPassword())
                .build();
        when(userRepository.save(any())).thenReturn(userBo);

        AddUserResponse response = userService.addUser(request);

        assertNotNull(response);
        assertEquals(request.getNickName(), response.getNickName());
        assertEquals(request.getFullName(), response.getFullName());
        assertEquals(request.getStatus(), response.getStatus());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void disconnectUser_ValidRequest_ReturnsDisconnectUserResponse() {
        DisconnectUserRequest request = new DisconnectUserRequest("nickname", null, Status.OFFLINE);
        UserBo userBo = UserBo.builder().nickName(request.getNickName()).status(Status.ONLINE).build();
        when(userRepository.findByNickName(any())).thenReturn(Optional.of(userBo));

        DisconnectUserResponse response = userService.disconnectUser(request);

        assertNotNull(response);
        assertEquals(request.getNickName(), response.getNickName());
        assertEquals(userBo.getFullName(), response.getFullName());
        assertEquals(request.getStatus(), response.getStatus());
        verify(userRepository, times(1)).findByNickName(any());
        verify(userRepository, times(1)).save(any());
    }

    // Write similar test cases for findConnectedUsers and login methods

    @Test
    public void testLogin_Successful() {
        // Mocking input request
        LoginUserRequest request = new LoginUserRequest();
        request.setToken("Basic " + Base64.getEncoder().encodeToString("username:password".getBytes()));

        // Mocking user retrieval
        UserBo userBo = new UserBo();
        userBo.setNickName("username");
        userBo.setFullName("John Doe");
        userBo.setStatus(Status.ONLINE);
        userBo.setPassword("password");
        when(userRepository.findByNickName("username")).thenReturn(Optional.of(userBo));

        // Perform the login
        LoginUserResponse response = userService.login(request);

        // Verify the response
        assertNotNull(response);
        assertEquals("username", response.getNickName());
        assertEquals("John Doe", response.getFullName());
        assertEquals(Status.ONLINE, response.getStatus());
    }

    public void testLogin_UserNotFound() {
        // Mocking input request
        LoginUserRequest request = new LoginUserRequest();
        request.setToken("Basic " + Base64.getEncoder().encodeToString("username:password".getBytes()));

        // Mocking user retrieval
        when(userRepository.findByNickName("username")).thenReturn(Optional.empty());

        // Perform the login (should throw an exception)
        Assert.assertThrows(ChatAppException.class, () ->  userService.login(request));
    }

    @Test
    public void testLogin_IncorrectPassword() {
        // Mocking input request
        LoginUserRequest request = new LoginUserRequest();
        request.setToken("Basic " + Base64.getEncoder().encodeToString("username:wrongpassword".getBytes()));

        // Mocking user retrieval
        UserBo userBo = new UserBo();
        userBo.setNickName("username");
        userBo.setPassword("correctpassword");
        when(userRepository.findByNickName("username")).thenReturn(Optional.of(userBo));

        // Perform the login (should throw an exception)
        Assert.assertThrows(ChatAppException.class, () ->  userService.login(request));
    }
}
