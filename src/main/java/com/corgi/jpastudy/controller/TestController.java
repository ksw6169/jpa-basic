package com.corgi.jpastudy.controller;

import com.corgi.jpastudy.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class TestController {

    private final EntityManagerFactory emf;

    @GetMapping(value = "/test")
    public ResponseEntity test() {

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Member member = new Member();
            member.setId(UUID.randomUUID().toString());
            member.setName("testName");

            em.persist(member);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
