package com.jinchae.demo.security;

import com.jinchae.demo.service.UserPrincipalDetailsService;
import lombok.Builder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 회원 인가 Filter
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserPrincipalDetailsService userPrincipalDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Builder
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserPrincipalDetailsService userPrincipalDetailsService, JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.userPrincipalDetailsService = userPrincipalDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        // header 에서 accessToken 정보 가져옴
        String token = request.getHeader(jwtTokenProvider.getHeaderName());

        try {
            // header 에 토큰이 없을 때
            if (StringUtils.isEmpty(token) || !token.startsWith(jwtTokenProvider.getTokenPrefix())) {
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (IOException | ServletException e) {
            logger.error("Could not set user authentication in security context: {}", e);
        }

    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {

        // 토큰으로부터 회원 memberId 가져옴
        String memberId = jwtTokenProvider.getSubjectFromToken(token);

        if (!StringUtils.isEmpty(memberId)) {
            return new UsernamePasswordAuthenticationToken(
                    memberId, null, userPrincipalDetailsService.loadUserByUsername(memberId).getAuthorities());
        }
        return null;
    }


}
