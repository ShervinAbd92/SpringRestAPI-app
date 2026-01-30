package com.shervin.store.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDto {

    @NotBlank(message = "Name is required")
    @Size(max = 244, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Email is required ")
    @Email(message = "Email must be valid")
    @Lowercase(message = "email should be lowecase")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 35, message = "password must be between 6 to 35 character long ")
    private String password;
}
