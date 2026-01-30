package com.shervin.store.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotBlank(message = "email is required")
    @Email
    private String email;

    @NotBlank(message = "password is required")
    private String password;
}
