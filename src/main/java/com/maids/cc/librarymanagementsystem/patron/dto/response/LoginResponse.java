package com.maids.cc.librarymanagementsystem.patron.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private String loginToken;
}
