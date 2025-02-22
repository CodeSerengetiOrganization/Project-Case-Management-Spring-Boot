package com.mytech.casemanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;

/*import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() throws Exception {
        JndiTemplate jndiTemplate = new JndiTemplate();
        return (DataSource) jndiTemplate.lookup("java:/MySqlDS");
    }
}*/
//    This will explicitly tell Spring to use the JNDI datasource java:/MySqlDS that you configured in WildFly.