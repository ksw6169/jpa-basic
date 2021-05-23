package com.corgi.jpastudy.learning.jpql;

import com.corgi.jpastudy.entity.Address;
import com.corgi.jpastudy.entity.Member;
import com.corgi.jpastudy.entity.Orders;
import com.corgi.jpastudy.entity.Team;
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
import java.util.Arrays;
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

        testMembers.forEach(member -> em.persist(member));

        String jpql = "select m from Member as m where m.username = 'kim'";
        List<Member> members = em.createQuery(jpql, Member.class).getResultList();

        assertEquals(1, members.size());
        assertEquals(members.get(0).getUsername(), "kim");
    }

    @Test
    public void findByCriteriaQuery() {

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
    }

    @Test
    public void findByNativeSql() {

        testMembers.forEach(member -> em.persist(member));

        String sql = "select member_id, name from member where name = 'kim'";
        List<Member> members = em.createNativeQuery(sql, Member.class).getResultList();

        assertEquals(1, members.size());
        assertEquals("kim", members.get(0).getUsername());
    }

    @Test
    public void findByQueryObject() {

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
    }

    @Test
    public void namedParameterBinding() {

        testMembers.forEach(member -> em.persist(member));

        Member member = em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class)
                .setParameter("username", testMembers.get(0).getUsername())
                .getSingleResult();

        assertEquals(testMembers.get(0).getUsername(), member.getUsername());
    }

    @Test
    public void positionedParameterBinding() {

        testMembers.forEach(member -> em.persist(member));

        String query = "SELECT m FROM Member m WHERE m.username = ?1";

        Member member = em.createQuery(query, Member.class)
                .setParameter(1, testMembers.get(0).getUsername())
                .getSingleResult();

        assertEquals(testMembers.get(0).getUsername(), member.getUsername());
    }

    @Test
    public void embeddedTypeProjection() {

        em.persist(testMembers.get(0));

        Address address = new Address("testCity", "testStreet", "testZipCode");
        Orders orders = new Orders(null, 10, testMembers.get(0), address);

        em.persist(orders);

        String query = "SELECT o.address FROM Orders o";
        Address resultAddress = em.createQuery(query, Address.class).getSingleResult();

        assertEquals(address.getStreet(), resultAddress.getStreet());
        assertEquals(address.getCity(), resultAddress.getCity());
        assertEquals(address.getZipcode(), resultAddress.getZipcode());
    }

    @Test
    public void scalaTypeProjection() {

        Address address = new Address("testCity", "testStreet", "testZipCode");

        Orders order1 = new Orders(null, 10, testMembers.get(0), address);
        Orders order2 = new Orders(null, 20, testMembers.get(0), address);

        order1.setMember(testMembers.get(0));
        order2.setMember(testMembers.get(1));

        testMembers.forEach(member -> em.persist(member));
        em.persist(order1);
        em.persist(order2);

        // 1. 기본 쿼리
        String basicQuery = "SELECT m.username FROM Member m";
        List<String> usernames = em.createQuery(basicQuery, String.class).getResultList();
        assertEquals(2, usernames.size());

        // 2. 통계 쿼리
        String statisticsQuery = "SELECT AVG(o.orderAmount) FROM Orders o";
        Double orderAmountAvg = em.createQuery(statisticsQuery, Double.class).getSingleResult();
        assertEquals(15.0, orderAmountAvg);

        // 3. 여러 값 조회
        Query query = em.createQuery("SELECT m.username, m.age FROM Member m");
        List<Object[]> resultList = query.getResultList();

        for (Object[] row : resultList) {
            String username = (String) row[0];
            Integer age = (Integer) row[1];
        }
    }

    @Test
    public void pagingQuery() {

        testMembers.forEach(member -> em.persist(member));

        String query = "SELECT m FROM Member m ORDER BY m.username DESC";
        TypedQuery<Member> typedQuery = em.createQuery(query, Member.class);
        typedQuery.setFirstResult(1);
        typedQuery.setMaxResults(20);

        List<Member> members = typedQuery.getResultList();

        assertEquals(1, members.size());
    }

    @Test
    public void innerjoin() {

        Team team = new Team();
        team.setName("TeamA");
        em.persist(team);

        testMembers.forEach(member -> {
            member.setTeam(team);
            em.persist(member);
        });

        String query = "SELECT m FROM Member m INNER JOIN m.team t";
        List<Member> members = em.createQuery(query, Member.class).getResultList();

        assertEquals(2, members.size());
    }

    @Test
    public void outerjoin() {

        Team team = new Team();
        team.setName("TeamA");
        em.persist(team);

        testMembers.forEach(member -> {
            member.setTeam(team);
            em.persist(member);
        });

        String query = "SELECT m FROM Member m LEFT JOIN m.team t";
        List<Member> members = em.createQuery(query, Member.class).getResultList();

        assertEquals(2, members.size());
    }

    @Test
    public void fetchjoin() {

        Team team = new Team();
        team.setName("TeamA");
        em.persist(team);

        testMembers.forEach(member -> {
            member.setTeam(team);
            em.persist(member);
        });

        String query = "SELECT m FROM Member m join fetch m.team";
        List<Member> members = em.createQuery(query, Member.class).getResultList();

        members.forEach(member -> {
            assertEquals("TeamA", member.getTeam().getName());
        });
    }

    @Test
    public void pathExpression() {

        Team team1 = new Team("TeamA");
        em.persist(team1);

        testMembers.forEach(member -> {
            member.setTeam(team1);
            em.persist(member);
        });

        Orders orders1 = new Orders(null, 1, testMembers.get(0), null);
        em.persist(orders1);

        // 상태 필드 경로 탐색
        String query1 = "select m.username, m.age from Member m";
        List<Object[]> results1 = em.createQuery(query1, Object[].class).getResultList();

        // 단일 값 연관 경로 탐색
        String query2 = "select o.member from Orders o";
        List<Member> results2 = em.createQuery(query2, Member.class).getResultList();

        // 컬렉션 값 연관 경로 탐색
        String query3 = "select t.members from Team t";
        List<Object[]> results3 = em.createQuery(query3, Object[].class).getResultList();

        assertEquals(2, results1.size());
        assertEquals(1, results2.size());
        assertEquals(2, results3.size());
    }

    @Test
    public void coalesce() {

        List<Member> members = Arrays.asList(
                new Member(null, "testName1", 20),
                new Member(null, null, 30),
                new Member(null, "testName3", 40));

        members.forEach(member -> em.persist(member));

        String query = "select coalesce(m.username, '이름 없는 회원') from Member m";
        List<String> usernames = em.createQuery(query, String.class).getResultList();

        usernames.forEach(username -> System.out.println("username : " + username));
    }

    @Test
    public void nullIf() {

        List<Member> members = Arrays.asList(
                new Member(null, "testName1", 20),
                new Member(null, null, 30),
                new Member(null, "관리자", 40));

        members.forEach(member -> em.persist(member));

        String query = "select nullif(m.username, '관리자') from Member m";
        List<String> usernames = em.createQuery(query, String.class).getResultList();

        usernames.forEach(username -> System.out.println("username : " + username));
    }

    @Test
    public void namedQuery() {

        testMembers.forEach(m -> em.persist(m));

        List<Member> members = em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", "lee")
                .getResultList();

        assertEquals(1, members.size());
    }
}
