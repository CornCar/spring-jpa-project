package com.cornCar.jpaShop.discount;
import com.cornCar.jpaShop.domain.Member;


public interface DiscountPolicy  {
    /**
     *
     * @return 할인 대상 금액
     *  */
    int discount(Member member, int price);
}
