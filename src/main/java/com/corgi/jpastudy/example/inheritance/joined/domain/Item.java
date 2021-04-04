package com.corgi.jpastudy.example.inheritance.joined.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn    // default column name : DTYPE, 어떤 서브타입 테이블의 데이터인지 분류하는 컬럼을 자동 생성해줌
public class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;
}
