package com.maids.cc.librarymanagementsystem.patron.service;

import com.maids.cc.librarymanagementsystem.generalresponse.Response;
import com.maids.cc.librarymanagementsystem.patron.dto.request.*;
import com.maids.cc.librarymanagementsystem.patron.dto.response.LoginResponse;
import com.maids.cc.librarymanagementsystem.patron.model.Patron;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface PatronService {
    Response registerPatron(RegistrationRequest registrationRequest);
    LoginResponse login(LoginRequest loginRequest);
    Patron getExistingPatron(String emailAddress);
    List<Patron> getAllPatrons();
    Response editPatronProfile(String emailAddress, EditProfileRequest editProfileRequest);
    Response deletePatron(String emailAddress);
    Response assignRoles(AssignRoleRequest assignRoleRequest);

}
