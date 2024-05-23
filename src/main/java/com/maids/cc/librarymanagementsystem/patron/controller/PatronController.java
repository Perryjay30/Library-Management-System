package com.maids.cc.librarymanagementsystem.patron.controller;

import com.maids.cc.librarymanagementsystem.patron.dto.request.*;
import com.maids.cc.librarymanagementsystem.patron.service.PatronService;
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
    public ResponseEntity<?> onboardPatron(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(patronService.registerPatron(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(patronService.login(loginRequest));
    }

    @GetMapping("/getExistingPatron/{emailAddress}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> getExistingPatron(@PathVariable String emailAddress) {
        return ResponseEntity.ok(patronService.getExistingPatron(emailAddress));
    }

    @GetMapping("/getAllPatrons")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAllPatrons() {
        return ResponseEntity.ok(patronService.getAllPatrons());
    }

    @PutMapping("/editPatronProfile/{emailAddress}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<?> editPatronProfile(@PathVariable String emailAddress, @Valid @RequestBody EditProfileRequest editProfileRequest) {
        return ResponseEntity.ok(patronService.editPatronProfile(emailAddress, editProfileRequest));
    }

    @DeleteMapping("/deletePatron/{emailAddress}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> deletePatron(@PathVariable String emailAddress) {
        return ResponseEntity.ok(patronService.deletePatron(emailAddress));
    }

    @PostMapping("/assignRoles")
    public ResponseEntity<?> assignUserRoles(@Valid @RequestBody AssignRoleRequest assignRoleRequest) {
        return ResponseEntity.ok(patronService.assignRoles(assignRoleRequest));
    }


}
