package com.corgi.jpastudy.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class Team {

    public Team(String name) {
        this.name = name;
    }

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public void addMember(Member member) {
        this.members.add(member);

        if (member.getTeam() != this) {
            member.setTeam(this);
        }
    }
}
