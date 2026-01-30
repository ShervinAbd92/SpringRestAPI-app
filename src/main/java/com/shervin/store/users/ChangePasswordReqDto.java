package com.shervin.store.users;

import lombok.Data;

@Data
public class ChangePasswordReqDto {
    private String oldPassword;
    private String newPassword;
}
