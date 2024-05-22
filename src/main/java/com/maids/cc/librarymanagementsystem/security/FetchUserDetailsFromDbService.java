package com.maids.cc.librarymanagementsystem.security;

import com.maids.cc.librarymanagementsystem.exception.LibraryManagementSystemException;
import com.maids.cc.librarymanagementsystem.patron.model.Patron;
import com.maids.cc.librarymanagementsystem.patron.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FetchUserDetailsFromDbService implements UserDetailsService {

    @Autowired
    private PatronRepository patronRepository;

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
       Optional<Patron> existingPatron = patronRepository.findByEmailAddress(emailAddress);
       return existingPatron.map(FetchUserDetailsFromDb::new).orElseThrow(() -> new LibraryManagementSystemException("User not found"));
    }
}
