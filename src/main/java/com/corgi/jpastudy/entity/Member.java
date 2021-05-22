package com.corgi.jpastudy.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Member {

    @Id
//    @GeneratedValue
    private String id;

    private String name;
}