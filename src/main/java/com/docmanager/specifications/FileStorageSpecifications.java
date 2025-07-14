package com.docmanager.specifications;

import com.docmanager.model.bo.FileStorageBO;
import com.docmanager.model.entity.FileStorage;
import com.docmanager.util.JsonUtils;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class FileStorageSpecifications {

    public static Specification<FileStorage> dynamic(FileStorageBO bo){
        log.debug("FileStorageBO: {}", JsonUtils.toJson(bo));
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (bo.getUuid() != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("uuid"), bo.getUuid()));
            }
            if (StringUtils.isNotBlank(bo.getFileName())) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("fileName")),
                    "%" + bo.getFileName().toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

