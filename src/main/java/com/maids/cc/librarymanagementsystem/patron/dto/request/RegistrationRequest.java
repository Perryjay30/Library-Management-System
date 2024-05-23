package com.maids.cc.librarymanagementsystem.patron.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Data
public class RegistrationRequest {
    @NotBlank(message = "This field is required")
    private String emailAddress;
    @NotBlank(message = "This field is required")
    private String firstName;
    @NotBlank(message = "This field is required")
    private String lastName;
    @NotBlank(message = "This field is required")
    private String password;
    @NotBlank(message = "This field is required")
    private String confirmPassword;

    public String getPassword() {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
