package com.corgi.jpastudy.config;

import com.corgi.jpastudy.properties.HibernateProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

@RequiredArgsConstructor
@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {

    private static final String[] SCAN_PACKAGES = { "com.corgi.jpastudy.domain" };

    private final HibernateProperties hibernateProperties;

    /**
     * LocalContainerEntityManagerFactoryBean
     * JPA를 스프링 컨테이너에서 사용할 수 있도록 스프링 프레임워크가 제공하는 기능
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(HikariDataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(SCAN_PACKAGES);                        // @Entity 탐색 위치 지정
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());    // 사용할 JPA 벤더 지정. 여기서는 Hibernate 구현체 사용
        em.setJpaProperties(additionalProperties());                // Hibernate 구현체의 속성 설정
//        em.setPersistenceUnitName("");                            // 영속성 유닛 이름 지정. 이름을 지정하지 않으면 "default" 사용

        return em;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory factory) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(factory);
        return transactionManager;
    }

    private Properties additionalProperties() {

        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", hibernateProperties.getHbm2ddlAuto());
        properties.put("hibernate.dialect", hibernateProperties.getDialect());
        properties.put("hibernate.show_sql", hibernateProperties.isShowSql());
        properties.put("hibernate.format_sql", hibernateProperties.isFormatSql());
        properties.put("hibernate.use_sql_comments", hibernateProperties.isUseSqlComments());
        properties.put("hibernate.jdbc.batch_size", hibernateProperties.getJdbcBatchSize());
//        properties.put("hibernate.id.new_generator_mappings", hibernateProperties.isNewGeneratorMappings());

        return properties;
    }
}
