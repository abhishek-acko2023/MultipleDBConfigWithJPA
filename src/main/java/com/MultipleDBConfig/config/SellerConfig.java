package com.MultipleDBConfig.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "sellerEntityManager",
        transactionManagerRef = "sellerTransactionManager",
        basePackages = {"com.MultipleDBConfig.Repository.Seller"})
public class SellerConfig {

    @Bean(name = "sellerSource")
    @ConfigurationProperties(prefix = "seller.datasource")
    public DataSource sellerSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sellerEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("sellerSource") DataSource sellerSource) {
        Map<String, String> primaryJpaProperties = new HashMap<>();
        primaryJpaProperties.put("hibernate.hbm2ddl.auto", "update");
        return builder
                .dataSource(sellerSource)
                .packages("com.MultipleDBConfig.Dao.Seller")
                .persistenceUnit("sellerSource")
                .properties(primaryJpaProperties)
                .build();
    }

    @Bean(name = "sellerTransactionManager")
    public PlatformTransactionManager transactionManager( @Qualifier("sellerEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
