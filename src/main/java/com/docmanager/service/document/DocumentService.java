package com.docmanager.service.document;

import com.docmanager.model.base.PageResponse;
import com.docmanager.model.dto.DocumentUpsertReqDTO;
import com.docmanager.model.vo.DocumentVO;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface DocumentService {
  PageResponse<DocumentVO> getAllDocuments(Pageable pageable);

  Optional<DocumentVO> findById(Long id);

  PageResponse<DocumentVO> findByFolderId(Long id, Pageable pageable);

  DocumentVO saveDocument(@Valid DocumentUpsertReqDTO documentUpsertReqDTO);

  DocumentVO updateDocument(@Valid DocumentUpsertReqDTO documentUpsertReqDTO);

  void deleteById(Long id);
}
