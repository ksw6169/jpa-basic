package com.corgi.jpastudy.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.jpa.properties.hibernate")
@Getter
@Setter
public class HibernateProperties {

    private boolean showSql;
    private String dialect;
    private boolean formatSql;
    private boolean useSqlComments;
    private String hbm2ddlAuto;
    private int jdbcBatchSize;
//    private boolean newGeneratorMappings;     // JPA에 맞춘 새로운 ID 생성 방법을 사용한다.
}
