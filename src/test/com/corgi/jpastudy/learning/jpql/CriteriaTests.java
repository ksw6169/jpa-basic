//package com.corgi.jpastudy.learning.jpql;
//
//import com.corgi.jpastudy.domain.Member;
//import com.corgi.jpastudy.entity.Team;
//import com.corgi.jpastudy.domain.dto.MemberDTO;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Tuple;
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.*;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//public class CriteriaTests {
//
//    @Autowired
//    private EntityManagerFactory emf;
//
//    private EntityManager em;
//    private List<Member> testMembers = new ArrayList<>();
//
//    @BeforeEach
//    public void setUp() {
//        em = emf.createEntityManager();
//        em.getTransaction().begin();
//
//        testMembers.add(new Member(null, "회원1", 30));
//        testMembers.add(new Member(null, "회원2", 20));
//    }
//
//    @AfterEach
//    public void after() {
//        if (em != null)
//            em.close();
//    }
//
//    @Test
//    public void basicQuery() {
//
//        /**
//         * JPQL
//         * select m from Member m
//         */
//        testMembers.forEach(m -> em.persist(m));
//
//        // Criteria 쿼리 생성에 필요한 Criteria 빌더 취득(EntityManager, EntityManagerFactory에서 취득 가능)
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//
//        // Criteria 생성, 반환 타입 지정
//        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
//
//        // FROM절 생성(반환된 값은 Criteria에서 사용하는 특별한 별칭)
//        Root<Member> m = cq.from(Member.class);
//
//        // SELECT절 생성
//        cq.select(m);
//
//        // 생성한 Criteria 쿼리 이용해서 조회
//        TypedQuery<Member> query = em.createQuery(cq);
//        List<Member> members = query.getResultList();
//
//        System.out.println("test");
//    }
//
//    @Test
//    public void whereAndOrder() {
//
//        /**
//         * JPQL
//         * select m from Member m
//         * where m.username = '회원1'
//         * order by m.age desc
//         */
//        testMembers.forEach(m -> em.persist(m));
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
//
//        Root<Member> m = cq.from(Member.class);     // FROM 절 생성
//
//        // 검색 조건 정의
//        Predicate usernameEqual = cb.equal(m.get("username"), "회원1");
//
//        // 정렬 조건 정의
//        Order ageDesc = cb.desc(m.get("age"));
//
//        // 쿼리 생성
//        cq.select(m)
//            .where(usernameEqual)   // WHERE 절 생성
//            .orderBy(ageDesc);      // ORDER BY 절 생성
//
//        List<Member> members = em.createQuery(cq).getResultList();
//
//        assertEquals(1, members.size());
//    }
//
//    @Test
//    public void predicate() {
//
//        /**
//         * JPQL
//         * select m from Member m
//         * where m.age > 10 order by m.age desc
//         */
//        testMembers.forEach(m -> em.persist(m));
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
//
//        Root<Member> m = cq.from(Member.class);
//
//        Predicate p = cb.greaterThan(m.get("age"), 10);
//
//        cq.select(m)
//            .where(p)
//            .orderBy(cb.desc(m.get("age")));
//
//        List<Member> members = em.createQuery(cq).getResultList();
//
//        assertEquals(2, members.size());
//    }
//
//    @Test
//    public void construct() {
//
//        testMembers.forEach(m -> em.persist(m));
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<MemberDTO> cq = cb.createQuery(MemberDTO.class);
//        Root<Member> m = cq.from(Member.class);
//
//        cq.select(cb.construct(MemberDTO.class, m.get("username"), m.get("age")));
//
//        TypedQuery<MemberDTO> query = em.createQuery(cq);
//        List<MemberDTO> results = query.getResultList();
//
//        assertEquals(2, results.size());
//    }
//
//    @Test
//    public void tuple() {
//
//        testMembers.forEach(m -> em.persist(m));
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
//        Root<Member> m = cq.from(Member.class);
//
//        cq.multiselect(
//                m.get("username").alias("username"),    // Tuple에서 사용할 튜플 별칭(필수)
//                m.get("age").alias("age"));
//
//        TypedQuery<Tuple> query = em.createQuery(cq);
//        List<Tuple> tuples = query.getResultList();
//
//        for (Tuple tuple : tuples) {
//            // 튜플 별칭으로 조회
//            String username = tuple.get("username", String.class);
//            Integer age = tuple.get("age", Integer.class);
//        }
//    }
//
//    @Test
//    public void groupByHaving() {
//
//        /**
//         * JPQL
//         * select m.team.name, max(m.age), min(m.age)
//         * from Member m
//         * group by m.team.name
//         * having min(m.age) > 10
//         */
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
//        Root<Member> m = cq.from(Member.class);
//
//        cq.multiselect(m.get("team").get("name"), cb.max(m.get("age")), cb.min(m.get("age")))
//                .groupBy(m.get("team").get("name"))
//                .having(cb.greaterThan(cb.min(m.get("age")), 10));      // HAVING MIN(m.age) > 10
//
//        TypedQuery<Object[]> query = em.createQuery(cq);
//        List<Object[]> results = query.getResultList();
//    }
//
//    @Test
//    public void join() {
//
//        Team team = new Team("팀A");
//        em.persist(team);
//
//        testMembers.forEach(m -> {
//            m.setTeam(team);
//            em.persist(m);
//        });
//
//        /**
//         * JPQL
//         * select m from Member m
//         * inner join m.team t
//         * where t.name = '팀A'
//         */
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
//
//        Root<Member> m = cq.from(Member.class);
//        Join<Member, Team> t = m.join("team", JoinType.INNER);  // INNER JOIN
//
//        cq.multiselect(m, t)
//            .where(cb.equal(t.get("name"), "팀A"));
//
//        TypedQuery<Object[]> query = em.createQuery(cq);
//        List<Object[]> results = query.getResultList();
//
//        assertEquals(2, results.size());
//    }
//
//    @Test
//    public void simpleSubQuery() {
//
//        testMembers.forEach(m -> em.persist(m));
//
//        /**
//         * JPQL
//         * select m from Member m
//         * where m.age >= (select AVG(m2.age) from Member m2)
//         */
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Member> mainQuery = cb.createQuery(Member.class);
//
//        // 서브 쿼리 생성
//        Subquery<Double> subQuery = mainQuery.subquery(Double.class);
//        Root<Member> m2 = subQuery.from(Member.class);
//        subQuery.select(cb.avg(m2.get("age")));
//
//        // 메인 쿼리 생성
//        Root<Member> m = mainQuery.from(Member.class);
//        mainQuery.select(m)
//                .where(cb.ge(m.get("age"), subQuery));
//
//        TypedQuery<Member> query = em.createQuery(mainQuery);
//        List<Member> results = query.getResultList();
//
//        assertEquals(1, results.size());
//    }
//
//    @Test
//    public void relationSubQuery() {
//
//        Team team = new Team("팀A");
//        em.persist(team);
//
//        testMembers.forEach(m -> {
//            m.setTeam(team);
//            em.persist(m);
//        });
//
//        /**
//         * JPQL
//         * select m from Member m
//         * where exists (select t from m.team t where t.name = '팀A')
//         */
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Member> mainQuery = cb.createQuery(Member.class);
//
//        // 서브쿼리에서 사용되는 메인 쿼리의 별칭
//        Root<Member> m = mainQuery.from(Member.class);
//
//        // 서브쿼리 생성
//        Subquery<Team> subQuery = mainQuery.subquery(Team.class);
//        Root<Member> subM = subQuery.correlate(m);      // 메인 쿼리의 별칭을 가져옴
//        Join<Member, Team> t = subM.join("team");
//        subQuery.select(t)
//                .where(cb.equal(t.get("name"), "팀A"));
//
//        // 메인 쿼리 생성
//        mainQuery.select(m)
//                .where(cb.exists(subQuery));
//
//        List<Member> results = em.createQuery(mainQuery).getResultList();
//
//        assertEquals(2, results.size());
//    }
//
//    @Test
//    public void in() {
//
//        testMembers.forEach(m -> em.persist(m));
//
//        /**
//         * JPQL
//         * select m from Member m
//         * where m.username in ("회원1", "회원2")
//         */
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
//
//        Root<Member> m = cq.from(Member.class);
//
//        cq.select(m)
//                .where(cb.in(m.get("username"))
//                        .value("회원1")
//                        .value("회원2"));
//
//        List<Member> members = em.createQuery(cq).getResultList();
//
//        assertEquals(2, members.size());
//    }
//
//    @Test
//    public void caseExpression() {
//
//        testMembers.forEach(m -> em.persist(m));
//
//        /**
//         * JPQL
//         * select m.username,
//         *  case when m.age >= 60 then 600
//         *       when m.age <= 15 then 500
//         *       else 100
//         *  end
//         *  from Member m
//         */
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
//
//        Root<Member> m = cq.from(Member.class);
//
//        cq.multiselect(m.get("username"),
//                cb.selectCase()
//                        .when(cb.ge(m.get("age"), 60), 600)
//                        .when(cb.le(m.get("age"), 15), 500)
//                        .otherwise(100));
//
//        List<Object[]> results = em.createQuery(cq).getResultList();
//
//        for (Object[] result : results) {
//            assertEquals(100, result[1]);
//        }
//    }
//
//    @Test
//    public void parameter() {
//
//        testMembers.forEach(m -> em.persist(m));
//
//        /**
//         * JPQL
//         * select m from Member m
//         * where m.username = :usernameParam
//         */
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
//
//        Root<Member> m = cq.from(Member.class);
//
//        cq.select(m)
//                .where(cb.equal(m.get("username"), cb.parameter(String.class, "usernameParam")));
//
//        List<Member> results = em.createQuery(cq)
//                .setParameter("usernameParam", "회원1")
//                .getResultList();
//
//        assertEquals(1, results.size());
//    }
//
//    @Test
//    public void dynamicQuery() {
//
//        Team team = new Team("팀A");
//        em.persist(team);
//
//        testMembers.forEach(m -> {
//            m.setTeam(team);
//            em.persist(m);
//        });
//
//        // 검색 조건
//        Integer age = 30;
//        String username = null;
//        String teamName = "팀A";
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
//
//        Root<Member> m = cq.from(Member.class);
//        Join<Member, Team> t = m.join("team");
//
//        List<Predicate> criteria = new ArrayList<>();
//
//        if (age != null)
//            criteria.add(cb.equal(m.get("age"), cb.parameter(Integer.class, "age")));
//        if (username != null)
//            criteria.add(cb.equal(m.get("username"), cb.parameter(String.class, "username")));
//        if (teamName != null)
//            criteria.add(cb.equal(t.get("name"), cb.parameter(String.class, "teamName")));
//
//        cq.where(cb.and(criteria.toArray(new Predicate[0])));
//
//        TypedQuery<Member> query = em.createQuery(cq);
//
//        if (age != null) query.setParameter("age", age);
//        if (username != null) query.setParameter("username", username);
//        if (teamName != null) query.setParameter("teamName", teamName);
//
//        List<Member> results = query.getResultList();
//
//        assertEquals(1, results.size());
//    }
//}
