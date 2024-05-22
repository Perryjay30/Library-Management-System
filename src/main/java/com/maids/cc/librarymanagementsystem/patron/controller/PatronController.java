package com.maids.cc.librarymanagementsystem.patron.controller;

import com.maids.cc.librarymanagementsystem.patron.dto.request.*;
import com.maids.cc.librarymanagementsystem.patron.service.PatronService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {
    private final PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }


    @PostMapping("/registration")
    public ResponseEntity<?> onboardPatron(@Valid @RequestBody ValidateEmail validateEmail) {
        return ResponseEntity.ok(patronService.registerPatron(validateEmail));
    }

    @PostMapping("/verifyOTP")
    public ResponseEntity<?> verifyOTP(@Valid @RequestBody VerifyOtpTokenRequest verifyOtpTokenRequest) {
        return ResponseEntity.ok(patronService.verifyOTP(verifyOtpTokenRequest));
    }

    @PostMapping("/createAccount/{emailAddress}")
    public ResponseEntity<?> createAccount(@PathVariable String emailAddress, @Valid @RequestBody RegistrationRequest registrationRequest){
        return ResponseEntity.ok(patronService.createPatronAccount(emailAddress, registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(patronService.login(loginRequest));
    }

    @GetMapping("/getExistingPatron/{emailAddress}")
    public ResponseEntity<?> getExistingPatron(@PathVariable String emailAddress) {
        return ResponseEntity.ok(patronService.getExistingPatron(emailAddress));
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> getAllPatrons() {
        return ResponseEntity.ok(patronService.getAllPatrons());
    }


    @PostMapping("/changePassword/{emailAddress}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> changePassword(@PathVariable String emailAddress, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(patronService.changePassword(emailAddress, changePasswordRequest));
    }

    @PostMapping("/forgotPassword")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return ResponseEntity.ok(patronService.forgotPassword(forgotPasswordRequest));
    }

    @PostMapping("/resetPassword/{emailAddress}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> resetPassword(@PathVariable String emailAddress, @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok(patronService.resetPassword(emailAddress, resetPasswordRequest));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(patronService.logout(request, response));
    }

    @PutMapping("/editPatronProfile/{emailAddress}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> editPatronProfile(@PathVariable String emailAddress, @Valid @RequestBody EditProfileRequest editProfileRequest) {
        return ResponseEntity.ok(patronService.editPatronProfile(emailAddress, editProfileRequest));
    }

    @DeleteMapping("/deletePatron/{emailAddress}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> deletePatron(@PathVariable String emailAddress) {
        return ResponseEntity.ok(patronService.deletePatron(emailAddress));
    }


}
