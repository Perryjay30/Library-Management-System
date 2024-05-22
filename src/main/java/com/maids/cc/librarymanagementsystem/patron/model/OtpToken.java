package com.maids.cc.librarymanagementsystem.patron.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@RequiredArgsConstructor
public class OtpToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime verifiedAt;
    @ManyToOne
    @JoinColumn(name="patron_id", referencedColumnName="patron_id")
    private Patron patron;

    public OtpToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt, Patron patron) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.patron = patron;
    }
}
