package com.corgi.jpastudy.example.inheritance.mappedSuperclass.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Entity 클래스는 @Entity 클래스거나, @MappedSuperclass 클래스인 경우에만 상속 가능
 */
@Getter
@Setter
@Entity
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;
}
