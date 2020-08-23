package com.jinchae.demo.entity;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

/**
 * 인가된 회원 정보
 *
 */
public class UserPrincipal implements UserDetails {

    private Long id;

    private String memberId;

    private String password;

    private String name;

    private LocalDateTime signedIn;

    private Collection<? extends GrantedAuthority> authorities;

    @Builder
    public UserPrincipal(Long id, String memberId, String name, String password, LocalDateTime signedIn,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.password = password;
        this.signedIn = signedIn;
        this.authorities = authorities;
    }

    public static UserPrincipal create(Member member) {
        return UserPrincipal.builder()
                .id(member.getId())
                .memberId(member.getMemberId())
                .signedIn(member.getSignedIn())
                .password(member.getPassword())
                .name(member.getName())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name())))
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.memberId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
