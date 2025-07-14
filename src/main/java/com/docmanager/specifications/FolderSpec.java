package com.docmanager.specifications;

import com.docmanager.model.bo.FolderBO;
import com.docmanager.model.entity.Folder;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class FolderSpec {

  public static Specification<Folder> dynamic(FolderBO bo) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (bo.getDeleteFlag() != null) {
        predicates.add(cb.equal(root.get("deleteFlag"), bo.getDeleteFlag()));
      } else {
        predicates.add(cb.isFalse(root.get("deleteFlag")));
      }
      if (bo.getId() != null) {
        predicates.add(cb.equal(root.get("id"), bo.getId()));
      }
      if (StringUtils.isNotBlank(bo.getFolderName())) {
        predicates.add(cb.equal(root.get("folderName"), bo.getFolderName()));
      }
      if (bo.getParentId() != null) {
        predicates.add(cb.equal(root.get("parent").get("id"), bo.getParentId()));
      }
      if (bo.getSortOrder() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("folderSort"), bo.getSortOrder()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

}
