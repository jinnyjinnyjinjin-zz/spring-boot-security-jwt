package com.jinchae.demo.service;

import com.jinchae.demo.dto.MemberDto;
import com.jinchae.demo.entity.Member;
import com.jinchae.demo.exception.DuplicatedMemberIdException;
import com.jinchae.demo.exception.MemberNotFoundException;
import com.jinchae.demo.repository.MemberRepository;
import com.jinchae.demo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 회원 Service
 *
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원 생성
     *
     * @param createReq 등록하려는 회원 정보
     * @return Member
     */
    public Member createMember(MemberDto.CreateReq createReq) {

        Optional<Member> originMember = memberRepository.findByMemberId(createReq.getMemberId());

        if (originMember.isPresent()) {
            throw DuplicatedMemberIdException.builder()
                    .fieldValue(createReq.getMemberId()).build();
        }

        Member member = Member.builder()
                .memberId(createReq.getMemberId())
                .name(createReq.getName())
                .password(createReq.getPassword())
                .build();

        return memberRepository.save(member);
    }

    /**
     * 회원 로그인
     *
     * @param loginReq 로그인 정보
     * @return String accessToken(JWT)
     */
    public String loginMember(MemberDto.LoginReq loginReq) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginReq.getMemberId(), loginReq.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Member member = getMemberByMemberId(loginReq.getMemberId());

        member.updateSignedIn(LocalDateTime.now());
        memberRepository.save(member);

        return jwtTokenProvider.generateToken(authentication);
    }

    /**
     * 회원 조회 by member id
     *
     * @param memberId 조회하려는 회원 id
     * @return Member
     */
    public Member getMemberByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> MemberNotFoundException.builder().message("등록되지 않은 회원입니다.").build());
    }

}
