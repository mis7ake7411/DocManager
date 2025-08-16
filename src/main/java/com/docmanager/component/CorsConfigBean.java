package com.docmanager.component;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cors.allowed")
public class CorsConfigBean {
  public List<String> origins;
  public List<String> methods;
  public List<String> headers;
}
