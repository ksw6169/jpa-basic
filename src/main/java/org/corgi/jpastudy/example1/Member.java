package org.corgi.jpastudy.example1;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Member {

    @Id
    private Long id;
    private String name;
}
