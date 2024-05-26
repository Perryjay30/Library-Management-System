package com.maids.cc.librarymanagementsystem.patron.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.maids.cc.librarymanagementsystem.exception.LibraryManagementSystemException;
import com.maids.cc.librarymanagementsystem.generalresponse.Response;
import com.maids.cc.librarymanagementsystem.patron.dto.request.AssignRoleRequest;
import com.maids.cc.librarymanagementsystem.patron.dto.request.EditProfileRequest;
import com.maids.cc.librarymanagementsystem.patron.dto.request.LoginRequest;
import com.maids.cc.librarymanagementsystem.patron.dto.request.RegistrationRequest;
import com.maids.cc.librarymanagementsystem.patron.dto.response.LoginResponse;
import com.maids.cc.librarymanagementsystem.patron.model.Patron;
import com.maids.cc.librarymanagementsystem.patron.repository.PatronRepository;
import com.maids.cc.librarymanagementsystem.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@EnableCaching
class PatronServiceImplTest {

    @Autowired
    private PatronService patronService;

    @MockBean
    private PatronRepository patronRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cacheManager.getCache("patrons").clear();
        cacheManager.getCache("allPatrons").clear();
    }

    @Test
    void testThatPatronCanRegister() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFirstName("Olayemi");
        registrationRequest.setLastName("Falaye");
        registrationRequest.setEmailAddress("melanin@perry.com");
        registrationRequest.setPassword("Agbaperry@97");
        registrationRequest.setConfirmPassword("Agbaperry@97");

        when(patronRepository.findByEmailAddress(registrationRequest.getEmailAddress())).thenReturn(Optional.empty());
        when(patronRepository.save(any(Patron.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Response response = patronService.registerPatron(registrationRequest);
        assertEquals("User registration successful!!", response.getMessage());
    }

    @Test
    void testThatPatronCannotRegisterWithExistingEmail() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFirstName("Olayemi");
        registrationRequest.setLastName("Falaye");
        registrationRequest.setEmailAddress("melanin@perry.com");
        registrationRequest.setPassword("Agbaperry@97");
        registrationRequest.setConfirmPassword("Agbaperry@97");

        when(patronRepository.findByEmailAddress(registrationRequest.getEmailAddress())).thenReturn(Optional.of(new Patron()));

        assertThrows(RuntimeException.class, () -> patronService.registerPatron(registrationRequest));
    }

    @Test
    void testThatPatronCannotRegisterWithMismatchedPasswords() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFirstName("Olayemi");
        registrationRequest.setLastName("Falaye");
        registrationRequest.setEmailAddress("melanin@perry.com");
        registrationRequest.setPassword("Agbaperry@97");
        registrationRequest.setConfirmPassword("WrongPassword");

        when(patronRepository.findByEmailAddress(registrationRequest.getEmailAddress())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> patronService.registerPatron(registrationRequest));
    }

    @Test
    void testThatPatronCanLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailAddress("melanin@perry.com");
        loginRequest.setPassword("Agbaperry@97");

        Patron patron = new Patron();
        patron.setEmailAddress(loginRequest.getEmailAddress());
        patron.setPassword(loginRequest.getPassword());

        when(patronRepository.findByEmailAddress(loginRequest.getEmailAddress())).thenReturn(Optional.of(patron));
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(loginRequest.getEmailAddress())).thenReturn("mockJwtToken");

        LoginResponse response = patronService.login(loginRequest);
        assertEquals("Login successful!!", response.getMessage());
        assertEquals("mockJwtToken", response.getLoginToken());
    }

    @Test
    void testThatPatronCannotLoginWithInvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailAddress("melanin@perry.com");
        loginRequest.setPassword("WrongPassword");

        Patron patron = new Patron();
        patron.setEmailAddress(loginRequest.getEmailAddress());
        patron.setPassword("CorrectPassword");

        when(patronRepository.findByEmailAddress(loginRequest.getEmailAddress())).thenReturn(Optional.of(patron));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> patronService.login(loginRequest));
    }

    @Test
    void testThatPatronCanBeRetrievedByEmailAddress() {
        String emailAddress = "mrjesus@gmail.com";
        Patron patron = new Patron();
        patron.setEmailAddress(emailAddress);

        when(patronRepository.findByEmailAddress(emailAddress)).thenReturn(Optional.of(patron));
        Patron retrievePatron = patronService.getExistingPatron(emailAddress);
        assertEquals(emailAddress, retrievePatron.getEmailAddress());

        Patron retrievePatron2 = patronService.getExistingPatron(emailAddress);
        assertEquals(emailAddress, retrievePatron2.getEmailAddress());

        verify(patronRepository, times(1)).findByEmailAddress(emailAddress);

    }

    @Test
    void testEditPatronProfile() {
        String emailAddress = "melanin@perry.com";
        EditProfileRequest editProfileRequest = new EditProfileRequest();
        editProfileRequest.setFirstName("NewFirstName");
        editProfileRequest.setLastName("NewLastName");
        editProfileRequest.setHomeAddress("New Address");
        editProfileRequest.setPhoneNumber("1234567890");

        Patron patron = new Patron();
        patron.setEmailAddress(emailAddress);

        when(patronRepository.findByEmailAddress(emailAddress)).thenReturn(Optional.of(patron));
        when(patronRepository.save(any(Patron.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Response response = patronService.editPatronProfile(emailAddress, editProfileRequest);
        assertEquals("Profile Updated Successfully", response.getMessage());
    }

    @Test
    void testDeletePatron() {
        String emailAddress = "melanin@perry.com";

        Patron patron = new Patron();
        patron.setEmailAddress(emailAddress);

        when(patronRepository.findByEmailAddress(emailAddress)).thenReturn(Optional.of(patron));

        Response response = patronService.deletePatron(emailAddress);
        assertEquals("Patron successfully deleted", response.getMessage());
        verify(patronRepository, times(1)).delete(patron);
    }

    @Test
    void testGetAllPatrons() {
        Patron patron1 = new Patron();
        patron1.setEmailAddress("zinchenko@perry.com");

        Patron patron2 = new Patron();
        patron2.setEmailAddress("kwanbala@perry.com");

        when(patronRepository.findAll()).thenReturn(List.of(patron1, patron2));

        List<Patron> patrons = patronService.getAllPatrons();
        assertEquals(2, patrons.size());

        List<Patron> patrons2 = patronService.getAllPatrons();
        assertEquals(2, patrons2.size());
        verify(patronRepository, times(1)).findAll();
    }

    @Test
    void testAssignRoles() {
        AssignRoleRequest assignRoleRequest = new AssignRoleRequest();
        assignRoleRequest.setEmailAddress("melanin@perry.com");
        assignRoleRequest.setUserRole("ADMIN");

        Patron patron = new Patron();
        patron.setEmailAddress(assignRoleRequest.getEmailAddress());

        when(patronRepository.findByEmailAddress(assignRoleRequest.getEmailAddress())).thenReturn(Optional.of(patron));
        when(patronRepository.save(any(Patron.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Response response = patronService.assignRoles(assignRoleRequest);
        assertEquals(patron.getFirstName() + " is now an  " + patron.getRole(), response.getMessage());
    }

    @Test
    void testAssignRoles_PatronNotFound() {
        AssignRoleRequest assignRoleRequest = new AssignRoleRequest();
        assignRoleRequest.setEmailAddress("nonexistent@perry.com");
        assignRoleRequest.setUserRole("ADMIN");

        when(patronRepository.findByEmailAddress(assignRoleRequest.getEmailAddress())).thenReturn(Optional.empty());

        assertThrows(LibraryManagementSystemException.class, () -> patronService.assignRoles(assignRoleRequest));
    }
}
