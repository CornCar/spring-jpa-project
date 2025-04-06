package com.cornCar.jpaShop.service;

import com.cornCar.jpaShop.domain.*;
import com.cornCar.jpaShop.domain.item.Item;
import com.cornCar.jpaShop.repository.ItemRepository;
import com.cornCar.jpaShop.repository.MemberRepository;
import com.cornCar.jpaShop.repository.OrderRepository;
import com.cornCar.jpaShop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    public int getDiscount(Member member,int price){ return orderRepository.discount(member,price); }

    /**
     * 주문
     */
    @Transactional
    public Long order(String memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findByUsername(memberId);
        Item item = itemRepository.findByUsername(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }
    //==생성 메서드==//
    public Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        int totalPrice = order.getTotalPrice();
        int discountPrice = getDiscount(member, totalPrice);
        order.setStatus(OrderStatus.ORDER);
        member.updatedBalance(totalPrice-discountPrice);
        member.setOrderCount(member.getOrderCount()+1);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findByUsername(orderId);
        //주문 취소
        order.cancel();
    }

    //검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
