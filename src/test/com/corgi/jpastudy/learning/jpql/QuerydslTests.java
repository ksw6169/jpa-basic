//package com.corgi.jpastudy.learning.jpql;
//
//import com.corgi.jpastudy.domain.Member;
//import com.querydsl.core.QueryModifiers;
//import com.querydsl.jpa.impl.JPAQuery;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//public class QuerydslTests {
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
//    @Test
//    public void querydsl() {
//
//        JPAQuery query = new JPAQuery(em);
//        QMember qMember = new QMember("m");     // 생성되는 JPQL의 별칭이 m
//
//        List<Member> members = query.from(qMember)
//                .where(qMember.name.eq("회원1"))
//                .orderBy(qMember.name.desc())
//                .list(qMember);
//    }
//
//    @Test
//    public void basicQuery() {
//
//        JPAQuery query = new JPAQuery(em);
//        QItem item = QItem.item;
//
//        List<Item> list = query.from(item)
//                .where(item.name.eq("좋은상품").and(item.price.gt(20000)))
//                .list(item);
//    }
//
//    @Test
//    public void pagingAndSort() {
//
//        JPAQuery query = new JPAQuery(em);
//        QItem item = QItem.item;
//
//        // 1.
//        List<Item> list = query.from(item)
//                .where(item.price.gt(20000))
//                .orderBy(item.price.desc(), item.stockQuantity.asc())
//                .offset(10).limit(20)
//                .list(item);
//
//        // 2.
//        QueryModifiers queryModifiers = new QueryModifiers(20L, 10L);       // limit, offset
//
//        List<Item> list = query.from(item)
//                .where(item.price.gt(20000))
//                .orderBy(item.price.desc(), item.stockQuantity.asc())
//                .restrict(queryModifiers)
//                .list(item);
//
//        // 3.
//        SearchResults<Item> result = query.from(item)
//                .where(item.price.gt(10000))
//                .offset(10).limit(20)
//                .listResults(item);
//
//        long total = result.getTotal();     // 검색된 전체 데이터 수
//        long limit = result.getLimit();
//        long offset = result.getOffset();
//        List<Item> results = result.getResults();    // 조회된 데이터
//    }
//
//    @Test
//    public void groupByHaving() {
//
//        query.from(item)
//                .groupBy(item.price)
//                .having(item.price.gt(1000))
//                .list(item);
//
//    }
//
//    @Test
//    public void join() {
//
//        QOrder order = QOrder.order;
//        QMember member = QMember.member;
//        QOrderItem orderItem = QOrderItem.orderItem;
//
//        query.from(order)
//                .join(order.member, member)
//                .leftJoin(order.orderItems, orderItem)
//                .list(order);
//    }
//
//
//
//}
