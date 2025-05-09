package com.cornCar.jpaShop.controller;

import com.cornCar.jpaShop.domain.Address;
import com.cornCar.jpaShop.domain.Member;
import com.cornCar.jpaShop.domain.member.Grade;
import com.cornCar.jpaShop.service.MemberService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/updateBalance")
    public String updateBalance(@RequestParam("balance") int balance,
                                @AuthenticationPrincipal UserDetails userDetails) {
        // 현재 로그인된 사용자 가져오기
        Member member = memberService.findByUsername(userDetails.getUsername());
        memberService.updateBalance(member, balance);
        return "redirect:/updateBalance"; // 업데이트 후 리디렉트
    }
/*
    @PostMapping("/updateBalance")
    public String updateBalance(@RequestParam String action, @ModelAttribute("member") Member member) {
        if ("increase".equals(action)) {
            member.setBalance(member.getBalance() + 1);  // 잔액 증가
        } else if ("decrease".equals(action)) {
            member.setBalance(member.getBalance() - 1);  // 잔액 감소
        }



        return "redirect:/memberPage";
    }*/
    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setId(form.getId());
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        member.setPassword(encodedPassword);
        member.setName(form.getName());
        member.setEmail(form.getEmail());
        member.setAddress(address);
        member.setGrade(Grade.BASIC);
        memberService.join(member);
        return "redirect:/login";
    }

    @GetMapping("/updateBalance")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/setting";
    }
}
