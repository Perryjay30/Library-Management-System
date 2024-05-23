package com.maids.cc.librarymanagementsystem.patron.repository;

import com.maids.cc.librarymanagementsystem.patron.model.Patron;
import com.maids.cc.librarymanagementsystem.patron.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface PatronRepository extends JpaRepository<Patron, Long> {
    Optional<Patron> findByEmailAddress(String emailAddress);
    Optional<Patron> findByRole(Role role);

}