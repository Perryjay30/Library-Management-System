package com.maids.cc.librarymanagementsystem.patron.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpTokenRequest {
    @NotBlank(message = "This field must not be empty")
    private String token;
}
