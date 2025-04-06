package com.cornCar.jpaShop.service;

import com.cornCar.jpaShop.domain.Member;
import com.cornCar.jpaShop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public String join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findById(member.getId());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findByUsername(String memberId) {
        return memberRepository.findByUsername(memberId);
    }

    public List<Member> findById(String id) {return  memberRepository.findById(id);}

    public void updateBalance(Member member,int balance) {
        if (member != null) {
            member.setBalance(balance); // balance 업데이트
            memberRepository.save(member); // DB 저장
        }
    }
}
