package org.corgi.jpastudy.exercise.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name="MEMBER_ID")
    private Long id;

    // 예시용 코드. 멤버 정보를 조회하는데, order를 같이 가져올 케이스가 많지 않을 것
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    private String name;
    private String city;

    private String street;
    private String zipCode;
}
