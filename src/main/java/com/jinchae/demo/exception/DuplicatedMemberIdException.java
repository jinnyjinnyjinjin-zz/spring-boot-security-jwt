package com.jinchae.demo.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 중복된 회원 아이디
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicatedMemberIdException extends RuntimeException {

    private Object fieldValue;

    @Builder
    public DuplicatedMemberIdException(Object fieldValue) {
        super(String.format("Duplicated id. fieldValue: %s", fieldValue));
        this.fieldValue = fieldValue;
    }
}
