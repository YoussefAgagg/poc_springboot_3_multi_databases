package com.acme.poc.springboot.backend.database.h2.configuration;

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
        basePackages = "com.acme.poc.springboot.backend.database.h2.repository",
        entityManagerFactoryRef = "h2EntityManagerFactory",
        transactionManagerRef = "h2UserTransactionManager"
)
@EntityScan(basePackages = {
        "com.acme.poc.springboot.backend.database.h2.entity"
})
public class H2Persistence {

    private final Environment env;


    public H2Persistence(Environment env) {
        this.env = env;
    }


    @Autowired
    @Bean("h2EntityManager")
    public EntityManager h2EntityManager(@Qualifier("h2EntityManagerFactory")
                                       LocalContainerEntityManagerFactoryBean factoryBean) {
        return factoryBean.getObject().createEntityManager();
    }

    @Autowired
    @Bean("h2UserTransactionManager")
    public PlatformTransactionManager h2UserTransactionManager(@Qualifier("h2EntityManagerFactory")
                                                                       LocalContainerEntityManagerFactoryBean factoryBean) {
        return new JpaTransactionManager() {{
            setEntityManagerFactory(factoryBean.getObject());
        }};
    }

    @Autowired
    @Bean("h2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean h2UserEntityManagerFactory(@Qualifier("h2UserDataSource")
                                                                                     DataSource h2UserDataSource) {
        Flyway.configure()
              .dataSource(h2UserDataSource)
              .locations("db/specific/h2")
              .load()
              .migrate();
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter() {{
            setGenerateDdl(true);
        }};
        HashMap<String, Object> properties = new HashMap<>() {{
            put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
            put("hibernate.dialect", env.getProperty("application.database.h2.hibernate.dialect"));
        }};
        PersistenceProvider provider = new HibernatePersistenceProvider();
        return new LocalContainerEntityManagerFactoryBean() {{
            setDataSource(h2UserDataSource);
            setPackagesToScan("com.acme.poc.springboot.backend.database.h2");
            setJpaVendorAdapter(vendorAdapter);
            setJpaPropertyMap(properties);
            setPersistenceProvider(provider);
        }};
    }

    @Bean("h2UserDataSource")
    public DataSource h2UserDataSource() throws ClassNotFoundException {

        return new DriverManagerDataSource() {{
            setDriverClassName(env.getProperty("application.database.h2.driverClassName"));
            setUrl(env.getProperty("application.database.h2.url"));
            setUsername(env.getProperty("application.database.h2.username"));
            setPassword(env.getProperty("application.database.h2.password"));
        }};
    }

}
