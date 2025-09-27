package com.docmanager.config;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * MDCTaskDecorator 用於在異步任務中傳遞 MDC (Mapped Diagnostic Context) 資訊。
 * 這對於在多線程環境中保持日誌上下文的一致性非常有用，例如在使用 @Async 或其他異步處理時。
 */
public class MDCTaskDecorator implements TaskDecorator {
  @Override
  public Runnable decorate(Runnable runnable) {
    Map<String, String> contextMap = MDC.getCopyOfContextMap(); // 取得當前線程的 MDC 資訊
    return () -> {
      Map<String, String> previous = MDC.getCopyOfContextMap(); // 保存當前線程的 MDC 資訊
      if (contextMap != null) {
        MDC.setContextMap(contextMap); // 設置 MDC 資訊到新線程
      } else {
        MDC.clear();
      }

      try {
        runnable.run();
      } finally {
        if (previous != null) {
          MDC.setContextMap(previous);
        } else {
          MDC.clear();
        }
      }
    };
  }
}
