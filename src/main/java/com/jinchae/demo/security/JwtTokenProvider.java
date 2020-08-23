package com.jinchae.demo.security;

import com.jinchae.demo.config.JwtAuthConfig;
import com.jinchae.demo.entity.Member;
import com.jinchae.demo.entity.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Jwt 관리
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final JwtAuthConfig jwtAuthConfig;


    public String generateToken(Authentication authentication) {

        UserPrincipal userPrinciple = (UserPrincipal) authentication.getPrincipal();
        SecretKey key = signingKey(jwtAuthConfig.getSecret());

        return Jwts.builder()
                .setSubject(userPrinciple.getUsername())
                .claim("role", userPrinciple.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(jwtAuthConfig.getExpiration())))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

//    public String generateToken(Member member) {
//
//        SecretKey key = signingKey(jwtAuthConfig.getSecret());
//
//        return Jwts.builder()
//                .setSubject(member.getMemberId())
//                .claim("role", Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name())))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(jwtAuthConfig.getExpiration())))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//    }

    public String getHeaderName() {
        return jwtAuthConfig.getHeaderName();
    }

    public String getTokenPrefix() {
        return jwtAuthConfig.getPrefix();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(jwtAuthConfig.getHeaderName());

        if (header != null && header.startsWith(jwtAuthConfig.getPrefix())) {
            return header.replace(jwtAuthConfig.getPrefix(), "");
        }

        return null;
    }

    public String getSubjectFromToken(String token) {

        try {

            return Jwts.parserBuilder()
                    .setSigningKey(signingKey(jwtAuthConfig.getSecret()))
                    .build()
                    .parseClaimsJws(token.replace(getTokenPrefix(), ""))
                    .getBody()
                    .getSubject();

        } catch (MalformedJwtException ex) {
            logger.warn("Invalid JWT token.");
        } catch (ExpiredJwtException ex) {
            logger.warn("Expired JWT token.");
        } catch (UnsupportedJwtException ex) {
            logger.warn("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.warn("JWT claims string is empty.");
        }

        return null;
    }

    private SecretKey signingKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

}
