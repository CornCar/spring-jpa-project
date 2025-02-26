package com.cornCar.jpaShop.controller;

import com.cornCar.jpaShop.controller.MemberForm;
import com.cornCar.jpaShop.domain.Address;
import com.cornCar.jpaShop.domain.Member;
import com.cornCar.jpaShop.domain.member.Role;
import com.cornCar.jpaShop.service.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

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
        member.setRole(Role.BASIC);
        memberService.join(member);
        return "redirect:/login";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
