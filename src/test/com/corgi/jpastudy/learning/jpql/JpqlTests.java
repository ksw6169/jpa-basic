package com.corgi.jpastudy.learning.jpql;

import com.corgi.jpastudy.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class JpqlTests {

    @Autowired
    private EntityManagerFactory emf;

    private EntityManager em;

    private List<Member> testMembers = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        em = emf.createEntityManager();
        em.getTransaction().begin();

        testMembers.add(new Member(null, "kim", 30));
        testMembers.add(new Member(null, "lee", 20));
    }

    @AfterEach
    public void after() {
        if (em != null)
            em.close();
    }

    @Test
    public void find() {

        try {
            testMembers.forEach(member -> em.persist(member));

            String jpql = "select m from Member as m where m.username = 'kim'";
            List<Member> members = em.createQuery(jpql, Member.class).getResultList();

            assertEquals(1, members.size());
            assertEquals(members.get(0).getUsername(), "kim");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByCriteriaQuery() {

        try {
            testMembers.forEach(member -> em.persist(member));

            // Criteria 사용 준비
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            // 루트 클래스(조회를 시작할 클래스)
            Root<Member> m = query.from(Member.class);

            // 쿼리 생성
            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
            List<Member> members = em.createQuery(cq).getResultList();

            assertEquals(1, members.size());
            assertEquals(members.get(0).getUsername(), "kim");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByNativeSql() {

        try {
            testMembers.forEach(member -> em.persist(member));

            String sql = "select member_id, name from member where name = 'kim'";
            List<Member> members = em.createNativeQuery(sql, Member.class).getResultList();

            assertEquals(1, members.size());
            assertEquals("kim", members.get(0).getUsername());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByQueryObject() {

        try {
            testMembers.forEach(member -> em.persist(member));

            // 1. TypedQuery 사용
            TypedQuery<Member> typedQuery = em.createQuery("SELECT m FROM Member m", Member.class);
            List<Member> members = typedQuery.getResultList();

            members.forEach(member -> System.out.println("member : " + member));

            // 2. Query 사용
            Query query = em.createQuery("SELECT m.username, m.age FROM Member m");
            List results = query.getResultList();

            results.forEach(o -> {
                Object[] result = (Object[]) o;
                System.out.println("username : " + result[0]);
                System.out.println("age : " + result[1]);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void namedParameterBinding() {

        try {
            testMembers.forEach(member -> em.persist(member));

            Member member = em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class)
                    .setParameter("username", testMembers.get(0).getUsername())
                    .getSingleResult();

            assertEquals(testMembers.get(0).getUsername(), member.getUsername());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void positionedParameterBinding() {

        try {
            testMembers.forEach(member -> em.persist(member));

            Member member = em.createQuery("SELECT m FROM Member m WHERE m.username = ?1", Member.class)
                    .setParameter(1, testMembers.get(0).getUsername())
                    .getSingleResult();

            assertEquals(testMembers.get(0).getUsername(), member.getUsername());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
