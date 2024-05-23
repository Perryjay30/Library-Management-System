package com.maids.cc.librarymanagementsystem.patron.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patron {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String firstName;
    private String lastName;
    @Pattern(regexp = "^\\d{1,11}$")
    private String phoneNumber;
    private String emailAddress;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
    private String password;
    private String patronUUID;
    private String homeAddress;
    private String dateJoined;
    private String dateModified;
}
