package com.jinchae.demo.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 등록되지 않은 회원
 *
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class MemberNotFoundException extends RuntimeException {

    @Builder
    public MemberNotFoundException(String message) {
        super(message);
    }

}
