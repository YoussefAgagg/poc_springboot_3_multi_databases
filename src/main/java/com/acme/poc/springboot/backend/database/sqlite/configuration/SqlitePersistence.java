package com.acme.poc.springboot.backend.database.sqlite.configuration;

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
        basePackages = "com.acme.poc.springboot.backend.database.sqlite.repository",
        entityManagerFactoryRef = "sqliteEntityManagerFactory",
        transactionManagerRef = "sqliteUserTransactionManager"
)
@EntityScan(basePackages = {
        "com.acme.poc.springboot.backend.database.sqlite.entity"
})
public class SqlitePersistence {

    private final Environment env;


    public SqlitePersistence(Environment env) {
        this.env = env;
    }


    @Autowired
    @Bean("sqliteEntityManager")
    public EntityManager sqliteEntityManager(@Qualifier("sqliteEntityManagerFactory")
                                       LocalContainerEntityManagerFactoryBean factoryBean) {
        return factoryBean.getObject().createEntityManager();
    }

    @Autowired
    @Bean("sqliteUserTransactionManager")
    public PlatformTransactionManager sqliteUserTransactionManager(@Qualifier("sqliteEntityManagerFactory")
                                                                       LocalContainerEntityManagerFactoryBean factoryBean) {
        return new JpaTransactionManager() {{
            setEntityManagerFactory(factoryBean.getObject());
        }};
    }

    @Autowired
    @Bean("sqliteEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sqliteUserEntityManagerFactory(@Qualifier("sqliteUserDataSource")
                                                                                     DataSource sqliteUserDataSource) {
        Flyway.configure()
              .dataSource(sqliteUserDataSource)
              .locations("db/specific/sqlite")
              .load()
              .migrate();
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter() {{
            setGenerateDdl(true);
        }};
        HashMap<String, Object> properties = new HashMap<>() {{
            put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
            put("hibernate.dialect", env.getProperty("application.database.sqlite.hibernate.dialect"));
            put("hibernate.show_sql","true");
            put("hibernate.ddl-auto","create");
        }};
        PersistenceProvider provider = new HibernatePersistenceProvider();
        return new LocalContainerEntityManagerFactoryBean() {{
            setDataSource(sqliteUserDataSource);
            setPackagesToScan("com.acme.poc.springboot.backend.database.sqlite");
            setJpaVendorAdapter(vendorAdapter);
            setJpaPropertyMap(properties);
            setPersistenceProvider(provider);
        }};
    }

    @Bean("sqliteUserDataSource")
    public DataSource sqliteUserDataSource() {
        return new DriverManagerDataSource() {{
            setDriverClassName(env.getProperty("application.database.sqlite.driverClassName"));
            setUrl(env.getProperty("application.database.sqlite.url"));
            setUsername(env.getProperty("application.database.sqlite.username"));
            setPassword(env.getProperty("application.database.sqlite.password"));
        }};
    }

}
