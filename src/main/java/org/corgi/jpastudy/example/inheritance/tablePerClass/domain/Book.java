package org.corgi.jpastudy.example.inheritance.tablePerClass.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
//@DiscriminatorValue(value = "B")   // default : Entity name
public class Book extends Item {

    private String author;
    private String isbn;
}
