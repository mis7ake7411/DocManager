package com.docmanager.service.document.Impl;

import com.docmanager.model.base.PageResponse;
import com.docmanager.model.entity.Document;
import com.docmanager.model.entity.FileStorage;
import com.docmanager.model.vo.DocumentVO;
import com.docmanager.repository.folderManager.DocumentRepository;
import com.docmanager.service.document.DocumentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
  private final DocumentRepository documentRepository;

  @Override
  public PageResponse<DocumentVO> getAllDocuments(Pageable pageable) {
    Page<Document> documentsPage = documentRepository.findAll(pageable);

    List<FileStorage> fileStorages = documentsPage.getContent()
        .stream()
        .map(Document::getFile)
        .toList();

    List<DocumentVO> documentVOs = documentsPage.getContent()
        .stream()
        .map(document -> DocumentVO.fromEntity(document, fileStorages))
        .toList();

    return PageResponse.of(documentVOs, documentsPage);
  }
}
