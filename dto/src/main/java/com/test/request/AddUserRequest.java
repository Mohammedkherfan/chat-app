package com.test.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NotNull(message = "Invalid request")
public class AddUserRequest {

    @NotBlank(message = "Invalid username")
    private String username;
    @NotBlank(message = "Invalid full name")
    private String fullName;
}