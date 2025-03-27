package com.cornCar.jpaShop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.cornCar.jpaShop.domain.member.Grade;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter @Setter
public class Member implements UserDetails {

    @Id
    @Column(name = "member_id")
    private String id;
    private String password;
    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private Grade grade;
    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
    @Override
    public String getUsername() {
        return this.id;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(grade.name()));
    }
    private int orderCount = 0;  // 주문 횟수 저장
    private int balance = 0;
    public void updatedBalance(int price) {
        if(balance < price) {
            balance -= price;
        }
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
