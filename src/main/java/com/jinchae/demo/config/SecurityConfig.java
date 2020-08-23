package com.jinchae.demo.config;

import com.jinchae.demo.security.JwtAuthorizationFilter;
import com.jinchae.demo.security.JwtTokenProvider;
import com.jinchae.demo.service.UserPrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;


@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserPrincipalDetailsService userPrincipalDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/v2/api-docs",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/webjars/**"
                );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
            .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);
        http
                .formLogin()
                .disable();

        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/v1/member/login", "/v1/member/join").permitAll()
                .antMatchers("/v1/member/info").hasRole("MEMBER")
                .anyRequest().authenticated();

        http
                .addFilter(new JwtAuthorizationFilter(authenticationManagerBean(), userPrincipalDetailsService, jwtTokenProvider));

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
