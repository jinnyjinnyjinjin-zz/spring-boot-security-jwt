package com.jinchae.demo;

import com.jinchae.demo.entity.Member;
import com.jinchae.demo.exception.MemberNotFoundException;
import com.jinchae.demo.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void saveMember_whenSaveMember_shouldReturnMember() {

        String memberId = "test@gmail.com";
        String name = "홍길동";
        String password = "qwer1234!@#$";

        Member member = Member.builder()
                .memberId(memberId)
                .name(name)
                .password(password)
                .build();

        Member savedMember = memberRepository.save(member);

        Assertions.assertThat(savedMember).isNotNull();
        Assertions.assertThat(savedMember.getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    public void findOneByMemberId_whenFindOne_shouldReturnMember() {

        String memberId = "test@gmail.com";

        Optional<Member> member = memberRepository.findByMemberId(memberId);

        Assertions.assertThat(member.isPresent()).isTrue();
        Assertions.assertThat(member.get().getMemberId()).isEqualTo(memberId);
    }

    @Test
    public void findOneByMemberId_whenFindOne_ShouldThrowsMemberNotFoundException() {

        String memberId = "test@gmail.com";
        String expectedMessage = "등록되지 않은 회원입니다.";

        memberRepository.findByMemberId(memberId);

        Exception exception = assertThrows(MemberNotFoundException.class, () -> {
            throw new MemberNotFoundException("등록되지 않은 회원입니다.");
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }
}
