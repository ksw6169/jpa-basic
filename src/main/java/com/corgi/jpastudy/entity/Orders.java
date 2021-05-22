package com.corgi.jpastudy.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Orders {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_amount")
    private int orderAmount;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Address address;

    public void setMember(Member member) {
        this.member = member;

        if (!member.getOrders().contains(this)) {
            member.addOrders(this);
        }
    }
}
