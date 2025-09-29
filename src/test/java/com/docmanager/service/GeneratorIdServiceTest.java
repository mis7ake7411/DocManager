package com.docmanager.service;

import com.docmanager.util.SnowflakeIdGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneratorIdServiceTest {

  @Mock
  private SnowflakeIdGenerator snowflakeIdGenerator;

  @InjectMocks
  private GeneratorIdService generatorIdService;

  @Test
  @DisplayName("createGeneratorId 方法應返回預期的 ID")
  void createGeneratorId_returnsExpectedId() {
    // Arrange
    long expectedId = 123456789L;
    when(snowflakeIdGenerator.nextId()).thenReturn(expectedId);

    // Act
    Long result = generatorIdService.createGeneratorId();

    // Assert
    assertThat(result).isEqualTo(expectedId);
  }

  @Test
  @DisplayName("使用真實的 SnowflakeIdGenerator 來測試 ID 生成的唯一性")
  void createGeneratorId_generatesUniqueIds() {
    SnowflakeIdGenerator realGenerator = new SnowflakeIdGenerator(1,1);
    GeneratorIdService realService = new GeneratorIdService(realGenerator);

    Long id1 = realService.createGeneratorId();
    Long id2 = realService.createGeneratorId();

    assertThat(id1).isNotNull();
    assertThat(id2).isNotNull();
    assertThat(id1).isNotEqualTo(id2);
    assertThat(id1).isPositive();
    assertThat(id2).isPositive();
  }
}

