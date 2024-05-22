package com.maids.cc.librarymanagementsystem.patron.service;

import com.maids.cc.librarymanagementsystem.exception.LibraryManagementSystemException;
import com.maids.cc.librarymanagementsystem.generalresponse.Response;
import com.maids.cc.librarymanagementsystem.notification.EmailSender;
import com.maids.cc.librarymanagementsystem.notification.EmailService;
import com.maids.cc.librarymanagementsystem.patron.dto.request.*;
import com.maids.cc.librarymanagementsystem.patron.dto.response.LoginResponse;
import com.maids.cc.librarymanagementsystem.patron.model.OtpToken;
import com.maids.cc.librarymanagementsystem.patron.model.Patron;
import com.maids.cc.librarymanagementsystem.patron.model.Token;
import com.maids.cc.librarymanagementsystem.patron.repository.OtpTokenRepository;
import com.maids.cc.librarymanagementsystem.patron.repository.PatronRepository;
import com.maids.cc.librarymanagementsystem.security.JwtService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.maids.cc.librarymanagementsystem.patron.model.Role.SUPER_ADMIN;
import static com.maids.cc.librarymanagementsystem.patron.model.Role.USER;
import static com.maids.cc.librarymanagementsystem.patron.model.Status.VERIFIED;

@Service
public class PatronServiceImpl implements PatronService {

    private final PatronRepository patronRepository;
    private final OtpTokenRepository otpTokenRepository;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;


    public PatronServiceImpl(OtpTokenRepository otpTokenRepository, PatronRepository patronRepository, JwtService jwtService,
                             EmailService emailService, AuthenticationManager authenticationManager) {
        this.otpTokenRepository = otpTokenRepository;
        this.patronRepository = patronRepository;
        this.jwtService = jwtService;
        this.emailService = emailService;
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
                        .firstName("Oluwapelumi").status(VERIFIED).role(SUPER_ADMIN).build();
                patronRepository.save(superAdmin);
            }
        } catch (LibraryManagementSystemException exception) {
            throw new LibraryManagementSystemException(exception.getMessage());
        }
    }

    @Override
    public String registerPatron(ValidateEmail validateEmail) {
        if(patronRepository.findByEmailAddress(validateEmail.getEmailAddress()).isPresent()) {
            throw new RuntimeException("Email Address already exists");
        } else {
            Patron newPatron = new Patron();
            newPatron.setEmailAddress(validateEmail.getEmailAddress());
           patronRepository.save(newPatron);
           return sendOTP(validateEmail.getEmailAddress());
        }
    }

    @Override
    public Response createPatronAccount(String emailAddress, RegistrationRequest registrationRequest) {
        Patron existingPatron = getExistingPatron(emailAddress);
        existingPatron.setFirstName(registrationRequest.getFirstName());
        existingPatron.setLastName(registrationRequest.getLastName());
        existingPatron.setPassword(registrationRequest.getPassword());
        existingPatron.setDateJoined(LocalDateTime.now().toString());
        existingPatron.setRole(USER);
        patronRepository.enableUser(VERIFIED, existingPatron.getEmailAddress());
        if(BCrypt.checkpw(registrationRequest.getConfirmPassword(), registrationRequest.getPassword())) {
            patronRepository.save(existingPatron);
            return new Response("User registration successful!!");
        } else {
            throw new IllegalStateException("Password does not match");
        }
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Patron existingPatron = getExistingPatron(loginRequest.getEmailAddress());
        if(existingPatron.getStatus() != VERIFIED) throw new LibraryManagementSystemException("User is not verified");
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


    public Response verifyOTP(VerifyOtpTokenRequest verifyOtpTokenRequest) {
        OtpToken foundToken = otpTokenRepository.findByToken(verifyOtpTokenRequest.getToken())
                .orElseThrow(() -> new LibraryManagementSystemException("Token doesn't exist!!!"));
        if(foundToken.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new LibraryManagementSystemException("Token has expired");
        if(foundToken.getVerifiedAt() != null)
            throw new LibraryManagementSystemException("Token has been used");
        if(!Objects.equals(verifyOtpTokenRequest.getToken(), foundToken.getToken()))
            throw new LibraryManagementSystemException("Incorrect Token");
        otpTokenRepository.setVerifiedAt(LocalDateTime.now(), verifyOtpTokenRequest.getToken());
        return new Response("Your email has been verified!!");
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        Patron existingPatron = getExistingPatron(forgotPasswordRequest.getEmailAddress());
        return generateOtpToken(forgotPasswordRequest.getEmailAddress(), existingPatron, emailService::sendNotificationEmailToResetPassword);
    }

    @Override
    public Response resetPassword(String emailAddress, ResetPasswordRequest resetPasswordRequest) {
        Patron existingPatron = getExistingPatron(emailAddress);
        existingPatron.setPassword(resetPasswordRequest.getPassword());
        if(BCrypt.checkpw(resetPasswordRequest.getConfirmPassword(), resetPasswordRequest.getPassword())) {
            patronRepository.save(existingPatron);
            return new Response("password reset successfully");
        } else
            throw new LibraryManagementSystemException("Try again, Password Mismatch!!");
    }

    public String sendOTP(String emailAddress) {
        Patron existingPatron = getExistingPatron(emailAddress);
        return generateOtpToken(emailAddress, existingPatron, emailService::sendNotificationEmailForRegistration);
    }

    @Override
    public Patron getExistingPatron(String emailAddress) {
        return patronRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new LibraryManagementSystemException("This user is not available!!"));
    }

    private String generateOtpToken(String emailAddress, Patron existingPatron, EmailSender emailSender) {
        String token = Token.generateToken(4);
        OtpToken otpToken = new OtpToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10L), existingPatron);
        otpTokenRepository.save(otpToken);
        String emailContent = emailService.buildEmailForRegistration(existingPatron.getFirstName(), token);
        emailSender.send(emailAddress, emailContent);
        return "Token successfully sent to your email Address. Please check!!";
    }

    @Override
    public Response changePassword(String emailAddress, ChangePasswordRequest changePasswordRequest) {
        Patron existingPatron = getExistingPatron(emailAddress);
        if(BCrypt.checkpw(changePasswordRequest.getOldPassword(), existingPatron.getPassword())) {
            existingPatron.setPassword(changePasswordRequest.getNewPassword());
            patronRepository.save(existingPatron);
            return new Response("Password Changed Successfully");
        } else throw new LibraryManagementSystemException("Old Password Incorrect!!");
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
        existingPatron.setDateModified(String.valueOf(LocalDateTime.now()));
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
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    @Override
    public Response logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return new Response("Logout successful");
    }
}
