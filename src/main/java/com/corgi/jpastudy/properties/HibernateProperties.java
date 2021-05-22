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
}
