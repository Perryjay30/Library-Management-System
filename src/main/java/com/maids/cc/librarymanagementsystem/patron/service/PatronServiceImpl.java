package com.maids.cc.librarymanagementsystem.patron.service;

import com.maids.cc.librarymanagementsystem.exception.LibraryManagementSystemException;
import com.maids.cc.librarymanagementsystem.generalresponse.Response;
import com.maids.cc.librarymanagementsystem.patron.dto.request.*;
import com.maids.cc.librarymanagementsystem.patron.dto.response.LoginResponse;
import com.maids.cc.librarymanagementsystem.patron.model.Patron;
import com.maids.cc.librarymanagementsystem.patron.model.Role;
import com.maids.cc.librarymanagementsystem.patron.repository.PatronRepository;
import com.maids.cc.librarymanagementsystem.security.JwtService;
import jakarta.annotation.PostConstruct;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.maids.cc.librarymanagementsystem.patron.model.Role.SUPER_ADMIN;
import static com.maids.cc.librarymanagementsystem.patron.model.Role.USER;

@Service
public class PatronServiceImpl implements PatronService {

    private final PatronRepository patronRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public PatronServiceImpl(PatronRepository patronRepository, JwtService jwtService,
                             AuthenticationManager authenticationManager) {
        this.patronRepository = patronRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostConstruct
    public void createSuperAdmin() {
        try {
            if (patronRepository.findByRole(SUPER_ADMIN).isEmpty()) {
                Patron superAdmin = Patron.builder()
                        .emailAddress("pelumijsh@gmail.com")
                        .password(BCrypt.hashpw("KingPerry@29", BCrypt.gensalt()))
                        .lastName("Taiwo")
                        .firstName("Oluwapelumi").role(SUPER_ADMIN).build();
                patronRepository.save(superAdmin);
            }
        } catch (LibraryManagementSystemException exception) {
            throw new LibraryManagementSystemException(exception.getMessage());
        }
    }


    @Override
    public Response registerPatron(RegistrationRequest registrationRequest) {
        if(patronRepository.findByEmailAddress(registrationRequest.getEmailAddress()).isPresent()) {
            throw new RuntimeException("Email Address already exists");
        } else {
            Patron newPatron = onboardPatron(registrationRequest);
            if(BCrypt.checkpw(registrationRequest.getConfirmPassword(), registrationRequest.getPassword())) {
                patronRepository.save(newPatron);
                return new Response("User registration successful!!");
            } else {
                throw new IllegalStateException("Password does not match");
            }
        }
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        getExistingPatron(loginRequest.getEmailAddress());
        Authentication authenticating = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmailAddress(), loginRequest.getPassword()));
        String loginMessage, jwtToken;
        if(authenticating.isAuthenticated()) {
            jwtToken = jwtService.generateToken(loginRequest.getEmailAddress());
            loginMessage = "Login successful!!";
            return new LoginResponse(loginMessage, jwtToken);
        } else
            throw new LibraryManagementSystemException("Invalid Credentials");
    }


    @Override
    @Cacheable(value = "patron", key = "#emailAddress")
    public Patron getExistingPatron(String emailAddress) {
        return patronRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new LibraryManagementSystemException("This user is not available!!"));
    }

    @Override
    public Response editPatronProfile(String emailAddress, EditProfileRequest editProfileRequest) {
        Patron existingPatron = getExistingPatron(emailAddress);
        existingPatron.setHomeAddress(editProfileRequest.getHomeAddress() != null && !editProfileRequest.getHomeAddress().equals(" ")
                ? editProfileRequest.getHomeAddress() : existingPatron.getHomeAddress());
        existingPatron.setFirstName(editProfileRequest.getFirstName() != null && !editProfileRequest.getFirstName().equals(" ")
                ? editProfileRequest.getFirstName() : existingPatron.getFirstName());
        existingPatron.setPhoneNumber(editProfileRequest.getPhoneNumber() != null && !editProfileRequest.getPhoneNumber().equals(" ")
                ? editProfileRequest.getPhoneNumber() : existingPatron.getPhoneNumber());
        existingPatron.setLastName(editProfileRequest.getLastName() != null && !editProfileRequest.getLastName().equals(" ")
                ? editProfileRequest.getLastName() : existingPatron.getLastName());
        existingPatron.setDateModified(LocalDateTime.now());
        patronRepository.save(existingPatron);
        return new Response("Profile Updated Successfully");
    }

    @Override
    public Response deletePatron(String emailAddress) {
        Patron existingPatron = getExistingPatron(emailAddress);
        patronRepository.delete(existingPatron);
        return new Response("Patron successfully deleted");
    }

    @Override
    @Cacheable("allPatrons")
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    @Override
    public Response assignRoles(AssignRoleRequest assignRoleRequest) {
        Patron existingPatron = patronRepository.findByEmailAddress(assignRoleRequest.getEmailAddress())
                .orElseThrow(() -> new LibraryManagementSystemException("Patron isn't available"));
        existingPatron.setRole(Role.valueOf(assignRoleRequest.getUserRole().toUpperCase()));
        patronRepository.save(existingPatron);
        return new Response(existingPatron.getFirstName() + " is now an  " + existingPatron.getRole());
    }

    private Patron onboardPatron(RegistrationRequest registrationRequest) {
        Patron newPatron = new Patron();
        newPatron.setEmailAddress(registrationRequest.getEmailAddress());
        newPatron.setFirstName(registrationRequest.getFirstName());
        newPatron.setLastName(registrationRequest.getLastName());
        newPatron.setPassword(registrationRequest.getPassword());
        newPatron.setPatronUUID(UUID.randomUUID().toString());
        newPatron.setDateJoined(LocalDateTime.now());
        newPatron.setRole(USER);
        return newPatron;
    }
}
