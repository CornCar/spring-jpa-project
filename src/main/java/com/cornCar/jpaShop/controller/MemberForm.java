package com.cornCar.jpaShop.controller;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;


@Getter @Setter
public class MemberForm {
    @NotEmpty(message = "id은 필수 입니다")
private String id;
    @NotEmpty(message = "비밀번호는 필수 입니다")
    private String password;
    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name;
    @NotEmpty(message = "이메일은 필수 입니다")
    private String email;
    private String city;
    private String street;
    private String zipcode;

}
