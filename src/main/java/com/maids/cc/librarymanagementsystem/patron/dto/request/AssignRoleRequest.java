package com.maids.cc.librarymanagementsystem.patron.dto.request;

import lombok.Data;

@Data
public class AssignRoleRequest {
    private String emailAddress;
    private String userRole;
}
