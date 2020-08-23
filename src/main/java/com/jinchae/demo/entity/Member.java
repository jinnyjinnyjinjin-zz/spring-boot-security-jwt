package com.jinchae.demo.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Member
 *
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false, length = 50)
    private String memberId;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 11)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime signedIn;

    @Builder
    public Member(String memberId, String password, String name) {
        this.memberId = memberId;
        this.password = encrypt(password);
        this.name = name;
        this.role = Role.ROLE_MEMBER;
    }

    private String encrypt(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public void updateSignedIn(LocalDateTime signedIn) {
        this.signedIn = signedIn;
    }
}
