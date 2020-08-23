package com.jinchae.demo.controller;

import com.jinchae.demo.dto.MemberDto;
import com.jinchae.demo.entity.Member;
import com.jinchae.demo.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;
import java.net.URI;

/**
 * 회원 등록/조회 API
 */
@RestController
@RequestMapping("/v1/member")
@RequiredArgsConstructor
@Api(tags = "회원 인증")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService memberService;

    /**
     * 회원 가입
     *
     * @param createReq 등록하려는 회원의 정보
     * @return 회원 등록 성공 여부 코드/메시지
     */
    @PostMapping("join")
    @ApiOperation(value = "회원 가입", notes = "회원 가입")
    public ResponseEntity join(@RequestBody @Valid MemberDto.CreateReq createReq) {

        logger.info("JoinReq: {}", createReq.getMemberId());

        Member member = memberService.createMember(createReq);
        boolean result = true;

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/v1/member/" + member.getId())
                .build().toUri();

        if (!member.getMemberId().equals(createReq.getMemberId())) {
            result = false;
        }

        return ResponseEntity.created(location).body(result);
    }

    /**
     * 로그인
     *
     * @return String accessToken
     */
    @PostMapping("login")
    @ApiOperation(value = "회원 로그인", notes = "회원 로그인")
    public ResponseEntity login(@RequestBody @Valid MemberDto.LoginReq loginReq) {

        logger.info("LoginReq: {}", loginReq.getMemberId());

        String accessToken = memberService.loginMember(loginReq);

        MemberDto.LoginRes loginRes = MemberDto.LoginRes.builder()
                .token(accessToken)
                .build();

        return ResponseEntity.ok().body(loginRes);
    }

    /**
     * 회원 정보
     *
     * @return MemberDto.DetailRes 회원 디테일 정보
     */
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("info")
    @ApiOperation(value = "회원 정보", notes = "회원 정보")
    public ResponseEntity info() {

        Object userPrincipal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        logger.info("MemberInfo userPrincipal: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        Member member = memberService.getMemberByMemberId(StringUtils.toString(userPrincipal));

        MemberDto.DetailRes response = MemberDto.DetailRes.builder()
                .member(member)
                .build();

        return ResponseEntity.ok().body(response);
    }

}
