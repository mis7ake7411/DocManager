package com.docmanager.service;

import com.docmanager.util.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GeneratorIdService {

  private final SnowflakeIdGenerator idGenerator;

  public GeneratorIdService(SnowflakeIdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  public Long createGeneratorId() {
    Long id = idGenerator.nextId();
    log.info("Generated ID: {}", id);

    return id;
  }
}

