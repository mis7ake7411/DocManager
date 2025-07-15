package com.docmanager.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceBean {
  private String url;
  private String username;
  private String password;
  private String driverClassName;
  private boolean passwordEncrypted;
  private String encryptionKey;
}
