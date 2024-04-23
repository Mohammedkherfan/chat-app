package com.test.request;

import com.test.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequest {

    private String nickName;
    private String fullName;
    private Status status;
    private String password;
}
