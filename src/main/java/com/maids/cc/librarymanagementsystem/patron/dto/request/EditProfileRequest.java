package com.maids.cc.librarymanagementsystem.patron.dto.request;

import lombok.Data;

@Data
public class EditProfileRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String homeAddress;
}
