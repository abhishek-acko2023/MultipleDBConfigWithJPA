# MultipleDBConfigWithJPA

## Objective
Setting up multiple Db Configs in `application.properties` using `@Configuration`.

e.g `application.properties` looks like below
```
# Server
server.port=3024

# Default DB Config
spring.datasource.jdbc-url=jdbc:postgresql://localhost:5432/customer
spring.datasource.username=postgres
spring.datasource.password=root
spring.datasource.driver-class-name=org.postgresql.Driver

# Other DB Config
seller.datasource.jdbc-url=jdbc:postgresql://localhost:5432/seller
seller.datasource.username=postgres
seller.datasource.password=root
seller.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
```

#### A little Knowledge about `application.properties` before we get started.

- application.properties are used by your application to generate 
Datasource which is then used to interact with DB. 
- By Default `spring.datasource` is used.
- However, we if we want to configure more than one DBs we can do so by changing 
the prefix of datasource e.g(`spring` in `spring.datasource`). There is no hardcore rule that prefix 
should only be changed, but it helps in maintaining clarity towards Db config.

## Basic Setup

- Set up a simple project with some basic dependencies as below :

```
  <dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-configuration-processor</artifactId>
			<optional>true</optional>
		</dependency>
	</dependencies>
```
- You can check out `pom.xml` to get all dependencies

- Setup Atleast 2 Different DBs

-  Create Controller, DAO, DTO, Service, Repository as present in project


## Important Note

- While Creating Model/Dao or Repository remember to make Separate Packages for those
which are required in Different Dbs.
- For Example if CustomerDao and SellerDao are required with Different DBs then 
CustomerDao should have directory something like `com.MultipleDBConfig.Dao.Customer`
- Sample Package Structure 
``` 
com.MultipleDBConfig
    |- DemoApplication.java
    |- Dao
        |- Customer
            |- CustomerDao.java
```

# Writing Configs for Different DBs

- Create a config package inside base package
``` 
com.MultipleDBConfig
    |- DemoApplication.java
    |- config
        |- primaryConfig.java
        |- secondaryConfig1.java
        |- secondaryConfig2.java
        |- ... so on
```

- One on the config should be marked a `@Primary` so that JPA can identify and give 
preference to the base datasource.
- For each config file we create `EntityManagerFactory`, `Transaction Manager` & `DataSource` 
to help JPA identify which db to use.

### Understanding @Primary DB Config File

```
package com.MultipleDBConfig.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration // --> Annotation Spring
@EnableTransactionManagement // --> Initiate Transaction Manager JPA
@EnableJpaRepositories(
        entityManagerFactoryRef = "customerEntityManager", // - Bean Names
        transactionManagerRef = "customerTransactionManager",
        basePackages = {"com.MultipleDBConfig.Repository.Customer"} // --> The Repository Package to be used by this config. Meaning all repositories inside this package will use same DB.
        ) 
public class CustomerConfig {

    @Primary
    @Bean(name = "customerSource")
    @ConfigurationProperties(prefix = "spring.datasource") // -> defined in application.properties
    public DataSource customerSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "customerEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("customerSource") DataSource customerSource) {
        Map<String, String> primaryJpaProperties = new HashMap<>();
        primaryJpaProperties.put("hibernate.hbm2ddl.auto", "update");
        
        // Building the datasource
        return builder
                .dataSource(customerSource)
                .packages("com.MultipleDBConfig.Dao.Customer") // -> Dao Package meaning all Daos inside this Package will use same DB
                .persistenceUnit("customerSource")
                .properties(primaryJpaProperties)
                .build();
    }

    @Primary
    @Bean(name = "customerTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("customerEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

```

- Remember to have all Bean Names unique.
- SecondaryConfig Files are same except they do not use the `@Primary` Annotation.

# FAQs

- Is there a change required in Controller, Dao, Dto, Repository, Service required ?
  - No. They will remain same.
- if we want to config multiple Db can we just do that with only secondaryDB configs 
meaning spring will take care of the primary one.
  - No we if there are more than one config we need to define a primaryConfig and then Secondary Configs


