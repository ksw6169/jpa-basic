package org.corgi.jpastudy.example.inheritance.joinStrategy.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
//@DiscriminatorValue(value = "A")   // default : Entity name
public class Album extends Item {

    private String artist;
}
