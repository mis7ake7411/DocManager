package com.docmanager.service.document;

import com.docmanager.model.vo.DocumentVO;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface DocumentService {
  List<DocumentVO> getAllDocuments(Pageable pageable);
}
