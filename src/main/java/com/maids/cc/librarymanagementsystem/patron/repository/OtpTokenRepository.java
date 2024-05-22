package com.maids.cc.librarymanagementsystem.patron.repository;

import com.maids.cc.librarymanagementsystem.patron.model.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Repository
@Transactional
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByToken(String token);

    @Modifying
    @Query("UPDATE OtpToken otpToken SET otpToken.verifiedAt=?1 WHERE otpToken.token= ?2")
    void setVerifiedAt(LocalDateTime verified, String token);
}
