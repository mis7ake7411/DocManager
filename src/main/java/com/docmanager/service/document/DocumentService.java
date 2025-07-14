package com.docmanager.service.document;

import com.docmanager.model.base.PageResponse;
import com.docmanager.model.vo.DocumentVO;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface DocumentService {
  PageResponse<DocumentVO> getAllDocuments(Pageable pageable);
}
