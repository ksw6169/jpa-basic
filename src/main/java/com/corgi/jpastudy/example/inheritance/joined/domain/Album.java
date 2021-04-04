package com.corgi.jpastudy.example.inheritance.joined.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
//@DiscriminatorValue(value = "A")   // default : Entity name
public class Album extends Item {

    private String artist;
}
