package com.maids.cc.librarymanagementsystem.patron.service;

import com.maids.cc.librarymanagementsystem.generalresponse.Response;
import com.maids.cc.librarymanagementsystem.patron.dto.request.*;
import com.maids.cc.librarymanagementsystem.patron.dto.response.LoginResponse;
import com.maids.cc.librarymanagementsystem.patron.model.Patron;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface PatronService {
    String registerPatron(ValidateEmail validateEmail);
    Response verifyOTP(VerifyOtpTokenRequest verifyOtpTokenRequest);
    Response createPatronAccount(String emailAddress, RegistrationRequest registrationRequest);
    LoginResponse login(LoginRequest loginRequest);
    Patron getExistingPatron(String emailAddress);
    String forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    Response resetPassword(String emailAddress, ResetPasswordRequest resetPasswordRequest);
    List<Patron> getAllPatrons();
    Response logout(HttpServletRequest request, HttpServletResponse response);
    Response changePassword(String emailAddress, ChangePasswordRequest changePasswordRequest);
    Response editPatronProfile(String emailAddress, EditProfileRequest editProfileRequest);
    Response deletePatron(String emailAddress);

}
