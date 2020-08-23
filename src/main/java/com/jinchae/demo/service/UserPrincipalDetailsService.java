package com.jinchae.demo.service;

import com.jinchae.demo.entity.Member;
import com.jinchae.demo.repository.MemberRepository;
import com.jinchae.demo.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserPrincipal 생성
 * 
 */
@Service
@RequiredArgsConstructor
public class UserPrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) {
        Optional<Member> member = memberRepository.findByMemberId(memberId);
        if (!member.isPresent()) {
            throw new UsernameNotFoundException(memberId);
        }
        return UserPrincipal.create(member.get());
    }

}