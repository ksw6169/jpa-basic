package org.corgi.jpastudy.example.oneToOne;

import org.corgi.jpastudy.example.oneToOne.domain.Locker;
import org.corgi.jpastudy.example.oneToOne.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class OneToOneMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Member member = em.find(Member.class, 2L);
            System.out.println(member.getLocker().getId());
            System.out.println(member.getLocker().getName());

            System.out.println(member.getLocker().getMember().getId());
            System.out.println(member.getLocker().getMember().getUsername());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
