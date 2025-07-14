package com.docmanager.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
  /**
   * 檢查資料是否存在
   */
  default boolean existsActiveById(ID id) {
    if (id == null) return false;
    Specification<T> spec = (root, query, cb) -> cb.and(
        cb.equal(root.get("id"), id)
    );
    return count(spec) > 0;
  }

  /**
   * 批次儲存並強制 flush
   */
  default List<T> batchSave(Collection<T> entities) {
    List<T> result = saveAll(entities);
    flush();
    return result;
  }
}
