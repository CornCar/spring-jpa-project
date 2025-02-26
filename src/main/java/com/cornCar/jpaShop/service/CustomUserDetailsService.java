package com.cornCar.jpaShop.service;
import jakarta.transaction.Transactional;
import com.cornCar.jpaShop.domain.Member;
import com.cornCar.jpaShop.domain.member.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberService.findOne(username);
        System.out.println("로그인 시도: " + username+ " member: "+member);
        if (member == null) {

            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
        if(member.getOrderCount() >=5) {
            member.setRole(Role.VIP);
        }
        return User.builder()
                .username(member.getId())
                .password(member.getPassword())
                .build();
    }

}