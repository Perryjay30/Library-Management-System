package com.maids.cc.librarymanagementsystem.patron.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "This field must not be empty")
    private String userPin;
    @NotBlank(message = "This field must not be empty")
    private String oldPassword;
    @NotBlank(message = "This field must not be empty")
    private String newPassword;


    public String getNewPassword() {
        return BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }
}
