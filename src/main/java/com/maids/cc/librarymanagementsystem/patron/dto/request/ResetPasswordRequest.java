package com.maids.cc.librarymanagementsystem.patron.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "This field must not be empty")
    private String password;
    @NotBlank(message = "This field must not be empty")
    private String confirmPassword;

    public String getPassword() {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
