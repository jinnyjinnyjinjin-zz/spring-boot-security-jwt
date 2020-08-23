package com.jinchae.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jinchae.demo.entity.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 회원 등록/조회를 위해 회원 정보를 담아 전달함
 */
@Getter
public class MemberDto {

    /**
     * 회원 생성 request
     */
    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static class CreateReq {

        @ApiModelProperty(example = "kildong@bithumbcorp.com")
        @NotBlank(message = "아이디는 필수 입력 값 입니다.")
        @Email(message = "아이디는 이메일 형식으로 입력 해 주세요.")
        @JsonProperty("member_id")
        private String memberId;

        @ApiModelProperty(example = "qwer1234!@#$")
        @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{12,}",
                message = "비밀번호는 영 소문자,영 대문자,숫자,특수문자 중 3가지 이상을 혼합하여 12자리 이상 입력 해 주세요.")
        private String password;

        @ApiModelProperty(example = "홍길동")
        @NotBlank(message = "이름은 필수 입력 값 입니다.")
        private String name;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static class LoginReq {

        @ApiModelProperty(example = "kildong@bithumbcorp.com")
        @NotBlank
        @JsonProperty("member_id")
        private String memberId;

        @ApiModelProperty(example = "qwer1234!@#$")
        @NotBlank
        private String password;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static class LoginRes {

        @JsonProperty("access_token")
        private String token;

        @Builder
        public LoginRes(String token) {
            this.token = token;
        }

    }

    /**
     * 회원 정보 조회 response
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static class DetailRes {

        @JsonProperty("member_id")
        private String memberId;

        private String name;

        @JsonProperty("signed_in")
        private LocalDateTime signedIn;

        @Builder
        public DetailRes(Member member) {
            this.memberId = member.getMemberId();
            this.name = member.getName();
            this.signedIn = member.getSignedIn();
        }

    }
}
