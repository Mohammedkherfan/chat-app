package com.test.bo;

import com.test.enums.ChatStatus;
import com.test.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBo {

    private Long id;
    private String username;
    private String fullName;
    private Status status;
    private ChatStatus chatStatus;
}
