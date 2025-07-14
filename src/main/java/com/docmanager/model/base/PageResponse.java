package com.docmanager.model.base;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public record PageResponse<T>(
    /* 分頁內容 */
    List<T> content,
    /* 當前頁碼 */
    int pageNumber,
    /* 每頁大小 */
    int pageSize,
    /* 總筆數 */
    long totalElements,
    /* 總頁數 */
    int totalPages
) {
  /**
   * 建立分頁回應物件
   *
   * @param content 分頁內容
   * @param page 分頁資訊
   */
  public static  <T, R>  PageResponse<R> of(List<R> content, Page<T> page) {
    return new PageResponse<>(
        content,
        page.getNumber() + 1, // 前端頁碼從 1 開始, 轉換為從 0 開始
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages()
    );
  }

  /**
   * 建立分頁回應物件
   *
   * @param page 分頁資訊
   */
  public static <T> PageResponse<T> ofPage(Page<T> page) {
    return new PageResponse<>(
        page.getContent(),
        page.getNumber() + 1,
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages()
    );
  }

  public static Pageable getPageable(int pageNumber, int pageSize) {
    return Pageable.ofSize(pageSize > 0 ? pageSize : 10) // 預設每頁 10 筆
        .withPage(Math.max(pageNumber - 1, 0)); // 前端頁碼從 1 開始, 轉換為從 0 開始
  }
}
