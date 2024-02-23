package org.apache.streampark.console.base.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Data
@Configuration
@ConfigurationProperties(prefix = "alert.datasource")
public class AlertDataSourceConfig {
    private String url;
    private String username;
    private String password;
    private String driverClassName;

    @Bean
    public JdbcTemplate alertJdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return new JdbcTemplate(dataSource);
    }

}
