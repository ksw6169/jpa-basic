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

    private static final String[] SCAN_PACKAGES = { "com.corgi.jpastudy.entity" };

    private final HibernateProperties hibernateProperties;

    private Properties additionalProperties() {

        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", hibernateProperties.getHbm2ddlAuto());
        properties.put("hibernate.dialect", hibernateProperties.getDialect());
        properties.put("hibernate.show_sql", hibernateProperties.isShowSql());
        properties.put("hibernate.format_sql", hibernateProperties.isFormatSql());
        properties.put("hibernate.use_sql_comments", hibernateProperties.isUseSqlComments());
        properties.put("hibernate.jdbc.batch_size", hibernateProperties.getJdbcBatchSize());

        return properties;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(HikariDataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(SCAN_PACKAGES);

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory factory) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(factory);

        return transactionManager;
    }
}
