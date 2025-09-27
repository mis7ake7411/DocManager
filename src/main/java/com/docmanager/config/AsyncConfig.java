package com.docmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * AsyncConfig 用於配置異步任務執行器 (ThreadPoolTaskExecutor)。
 * 它設置了線程池的核心大小、最大大小、隊列容量以及線程名稱前綴。
 * 此外，它還使用 MDCTaskDecorator 來確保在異步任務中傳遞 MDC (Mapped Diagnostic Context) 資訊，
 * 以便在多線程環境中保持日誌上下文的一致性。
 */
@Configuration
public class AsyncConfig {
  @Bean(name = "DocManagerTaskExecutor")
  public ThreadPoolTaskExecutor applicationTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4); // 設置核心線程數量
    executor.setMaxPoolSize(8);  // 設置最大線程數量
    executor.setQueueCapacity(100); // 設置隊列容量
    executor.setThreadNamePrefix("DocManagerAsync-"); // 設置線程名稱前綴
    executor.setTaskDecorator(new MDCTaskDecorator()); // 使用 MDCTaskDecorator 傳遞 MDC 資訊
    executor.initialize();
    return executor;
  }
}