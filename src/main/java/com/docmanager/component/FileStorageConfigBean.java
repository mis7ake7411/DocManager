package com.docmanager.component;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "file-storage.file")
public class FileStorageConfigBean {
  private String uploadPath;
  private DataSize maxUploadSize;
  private Integer downloadCacheTime;
  private List<String> allowedTypes;
}
