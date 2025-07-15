package com.docmanager.component;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "file-storage.file")
public class FileStorageConfigBean {
  private String uploadPath;
  private String maxUploadSize;
  private String downloadCacheTime;
  private List<String> allowedTypes;
}
