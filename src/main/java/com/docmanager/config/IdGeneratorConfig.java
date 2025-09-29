package com.docmanager.config;

import com.docmanager.util.SnowflakeIdGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "id-generator")
public class IdGeneratorConfig {
  private long datacenterId;
  private long machineId;

  @Bean
  public SnowflakeIdGenerator snowflakeIdGenerator() {
    return new SnowflakeIdGenerator(datacenterId, machineId);
  }
}

