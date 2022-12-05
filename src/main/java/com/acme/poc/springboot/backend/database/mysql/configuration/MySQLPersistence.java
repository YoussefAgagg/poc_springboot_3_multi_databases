package com.acme.poc.springboot.backend.database.mysql.configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.spi.PersistenceProvider;
import org.flywaydb.core.Flyway;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.acme.poc.springboot.backend.database.mysql.repository",
        entityManagerFactoryRef = "mysqlEntityManagerFactory",
        transactionManagerRef = "mysqlUserTransactionManager"
)
@EntityScan(basePackages = {
        "com.acme.poc.springboot.backend.database.mysql.entity"
})
public class MySQLPersistence {

    private final Environment env;


    public MySQLPersistence(Environment env) {
        this.env = env;
    }


    @Autowired
    @Bean("mysqlEntityManager")
    public EntityManager mysqlEntityManager(@Qualifier("mysqlEntityManagerFactory")
                                       LocalContainerEntityManagerFactoryBean factoryBean) {
        return factoryBean.getObject().createEntityManager();
    }

    @Autowired
    @Bean("mysqlUserTransactionManager")
    public PlatformTransactionManager mysqlUserTransactionManager(@Qualifier("mysqlEntityManagerFactory")
                                                                       LocalContainerEntityManagerFactoryBean factoryBean) {
        return new JpaTransactionManager() {{
            setEntityManagerFactory(factoryBean.getObject());
        }};
    }

    @Autowired
    @Bean("mysqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mysqlUserEntityManagerFactory(@Qualifier("mysqlUserDataSource")
                                                                                     DataSource mysqlUserDataSource) {
        Flyway.configure()
              .dataSource(mysqlUserDataSource)
              .locations("db/specific/mysql")
              .load()
              .migrate();
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter() {{
            setGenerateDdl(true);
        }};
        HashMap<String, Object> properties = new HashMap<>() {{
            put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
            put("hibernate.dialect", env.getProperty("application.database.mysql.hibernate.dialect"));
        }};
        PersistenceProvider provider = new HibernatePersistenceProvider();
        return new LocalContainerEntityManagerFactoryBean() {{
            setDataSource(mysqlUserDataSource);
            setPackagesToScan("com.acme.poc.springboot.backend.database.mysql");
            setJpaVendorAdapter(vendorAdapter);
            setJpaPropertyMap(properties);
            setPersistenceProvider(provider);
        }};
    }

    @Bean("mysqlUserDataSource")
    public DataSource mysqlUserDataSource() {
        return new DriverManagerDataSource() {{
            setDriverClassName(env.getProperty("application.database.mysql.driverClassName"));
            setUrl(env.getProperty("application.database.mysql.url"));
            setUsername(env.getProperty("application.database.mysql.username"));
            setPassword(env.getProperty("application.database.mysql.password"));
        }};
    }

}
