package com.docmanager.config;

import com.docmanager.component.DataSourceBean;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableEncryptableProperties
@EnableJpaRepositories(
    basePackages = "com.docmanager.repository",
    entityManagerFactoryRef = "documentEntityManagerFactory",
    transactionManagerRef = "documentTransactionManager"
)
@EnableTransactionManagement
@RequiredArgsConstructor
@Slf4j
public class JpaDatasourceConfig {
  private final DataSourceBean dataSourceBean;
  private final JpaProperties jpaProperties;

  @Primary
  @Bean(name = "documentDataSource")
  public DataSource documentDataSource() {
      HikariDataSource dataSource = new HikariDataSource();
      dataSource.setJdbcUrl(dataSourceBean.getUrl());
      dataSource.setUsername(dataSourceBean.getUsername());
      dataSource.setPassword(dataSourceBean.getPassword());
      dataSource.setDriverClassName(dataSourceBean.getDriverClassName());
      return dataSource;
  }

  @Primary
  @Bean(name = "documentEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean documentEntityManagerFactory(
      @Qualifier("documentDataSource") DataSource dataSource,
      EntityManagerFactoryBuilder builder ) {

    Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
    return builder
        .dataSource(dataSource)
        .packages("com.docmanager.model.entity")
        .persistenceUnit("documentPU")
        .properties(properties)
        .build();
  }

  @Primary
  @Bean(name = "documentTransactionManager")
  public PlatformTransactionManager documentTransactionManager(
      @Qualifier("documentEntityManagerFactory") EntityManagerFactory documentEntityManagerFactory) {
    return new JpaTransactionManager(documentEntityManagerFactory);
  }
}