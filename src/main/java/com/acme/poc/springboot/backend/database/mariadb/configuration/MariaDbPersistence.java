package com.acme.poc.springboot.backend.database.mariadb.configuration;

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
        basePackages = "com.acme.poc.springboot.backend.database.mariadb.repository",
        entityManagerFactoryRef = "mariadbEntityManagerFactory",
        transactionManagerRef = "mariadbUserTransactionManager"
)
@EntityScan(basePackages = {
        "com.acme.poc.springboot.backend.database.mariadb.entity"
})
public class MariaDbPersistence {

    private final Environment env;


    public MariaDbPersistence(Environment env) {
        this.env = env;
    }


    @Autowired
    @Bean("mariadbEntityManager")
    public EntityManager mariadbEntityManager(@Qualifier("mariadbEntityManagerFactory")
                                       LocalContainerEntityManagerFactoryBean factoryBean) {
        return factoryBean.getObject().createEntityManager();
    }

    @Autowired
    @Bean("mariadbUserTransactionManager")
    public PlatformTransactionManager mariadbUserTransactionManager(@Qualifier("mariadbEntityManagerFactory")
                                                                       LocalContainerEntityManagerFactoryBean factoryBean) {
        return new JpaTransactionManager() {{
            setEntityManagerFactory(factoryBean.getObject());
        }};
    }

    @Autowired
    @Bean("mariadbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mariadbUserEntityManagerFactory(@Qualifier("mariadbUserDataSource")
                                                                                     DataSource mariadbUserDataSource) throws ClassNotFoundException {
        Flyway.configure()
              .dataSource(mariadbUserDataSource)
              .locations("db/specific/mariadb")
              .load()
              .migrate();
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter() {{
            setGenerateDdl(true);
        }};
        HashMap<String, Object> properties = new HashMap<>() {{
            put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
            put("hibernate.dialect", env.getProperty("application.database.mariadb.hibernate.dialect"));
        }};
        PersistenceProvider provider = new HibernatePersistenceProvider();
        return new LocalContainerEntityManagerFactoryBean() {{
            setDataSource(mariadbUserDataSource);
            setPackagesToScan("com.acme.poc.springboot.backend.database.mariadb");
            setJpaVendorAdapter(vendorAdapter);
            setJpaPropertyMap(properties);
            setPersistenceProvider(provider);
        }};
    }

    @Bean("mariadbUserDataSource")
    public DataSource mariadbUserDataSource() {
        return new DriverManagerDataSource() {{
            setDriverClassName(env.getProperty("application.database.mariadb.driverClassName"));
            setUrl(env.getProperty("application.database.mariadb.url"));
            setUsername(env.getProperty("application.database.mariadb.username"));
            setPassword(env.getProperty("application.database.mariadb.password"));
        }};
    }

}
