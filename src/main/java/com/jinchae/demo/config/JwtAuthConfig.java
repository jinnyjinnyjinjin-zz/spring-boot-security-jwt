package com.jinchae.demo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Jwt 생성에 필요한 정보
 *
 */
@Getter
@Configuration
public class JwtAuthConfig {

    @Value("${security.jwt.header:Authorization}")
    private String headerName;                                                      // 헤더 이름 'Authorization'

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;                                                          // 토큰 prefix 'Bearer '

    @Value("${security.jwt.expiration:#{24 * 60 * 60}}")
    private int expiration;                                                         // 토큰 만료 시간

    @Value("${security.jwt.secret:bNj0DTLrRXPpoVjdtK8g9aJJj7pPXyRP8lOxpYBVb1mN6VLgfGAYiOA4g0XYAW6}")
    private String secret;                                                          // jwt signing 을 위한 secret

}
