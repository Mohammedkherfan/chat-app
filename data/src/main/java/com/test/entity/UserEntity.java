package com.test.entity;

import com.test.enums.ChatStatus;
import com.test.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class UserEntity {

    @Id
    private Long id;
    private String userExternalId;
    private String username;
    private String fullName;
    private Status status;
    private ChatStatus chatStatus;
}
