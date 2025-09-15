package com.docmanager.service.document.Impl;

import com.docmanager.model.base.PageResponse;
import com.docmanager.model.dto.DocumentUpsertReqDTO;
import com.docmanager.model.entity.Document;
import com.docmanager.model.entity.FileStorage;
import com.docmanager.model.entity.Folder;
import com.docmanager.model.vo.DocumentVO;
import com.docmanager.repository.folderManager.DocumentRepository;
import com.docmanager.repository.folderManager.FileStorageRepository;
import com.docmanager.repository.folderManager.FolderRepository;
import com.docmanager.service.document.DocumentService;
import com.docmanager.util.JsonUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
  private final FileStorageRepository fileStorageRepository;
  private final FolderRepository folderRepository;
  private final DocumentRepository documentRepository;

  @Override
  @Transactional(readOnly=true)
  public PageResponse<DocumentVO> getAllDocuments(Pageable pageable) {
    Page<Document> documentsPage = documentRepository.findAll(pageable);

    List<DocumentVO> documentVOs = documentsPage.getContent()
        .stream()
        .map(DocumentVO::fromEntity)
        .toList();

    return PageResponse.of(documentVOs, documentsPage);
  }

  @Override
  @Transactional(readOnly=true)
  public Optional<DocumentVO> findById(Long id) {
    return documentRepository.findById(id)
        .map(DocumentVO::fromEntity);
  }

  @Override
  public DocumentVO saveDocument(DocumentUpsertReqDTO documentUpsertReqDTO) {
    Optional<FileStorage> file = fileStorageRepository.findById(documentUpsertReqDTO.fileStorageId());
    Optional<Folder> folder = folderRepository.findById(documentUpsertReqDTO.folderId());
    Document document = Document.builder()
        .documentName(documentUpsertReqDTO.documentName())
        .documentVersion(getNextVersion(documentUpsertReqDTO.documentName(), documentUpsertReqDTO.folderId()))
        .description(documentUpsertReqDTO.description())
        .createdTime(LocalDateTime.now())
        .folder(folder.orElse(null))
        .file(file.orElse(null))
        .deleteFlag(false)
        .build();

    Document savedDocument = documentRepository.save(document);

    return DocumentVO.fromEntity(savedDocument);
  }

  @Override
  public DocumentVO updateDocument(DocumentUpsertReqDTO documentUpsertReqDTO) {
    Document existingDocument = documentRepository.findById(documentUpsertReqDTO.id())
        .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + documentUpsertReqDTO.id()));

    // 更新文件屬性
    existingDocument.setDocumentName(documentUpsertReqDTO.documentName());
    existingDocument.setDescription(documentUpsertReqDTO.description());
    existingDocument.setModifiedTime(LocalDateTime.now());

    // 如果文件存儲ID有變更，則更新文件存儲
    if (documentUpsertReqDTO.fileStorageId() != null) {
      FileStorage file = fileStorageRepository.getReferenceById(documentUpsertReqDTO.fileStorageId());
      existingDocument.setFile(file);
    }

    Document updatedDocument = documentRepository.save(existingDocument);

    return DocumentVO.fromEntity(updatedDocument);
  }

  @Override
  public void deleteById(Long id) {
    Document document = documentRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + id));
    document.setDeleteFlag(true); // 標記為已刪除
  }

  private String getNextVersion(String documentName, Long folderId) {
    if(StringUtils.isNotBlank(documentName)){
      // 取得相同檔名中的最大版本號
      String maxVersion = documentRepository.findMaxDocumentVersion(documentName, folderId);
      log.info("{}, 目前文件最大版號 : {}", documentName, maxVersion);
      if(StringUtils.isNotBlank(maxVersion)){
        String[] versionParts = maxVersion.split("_");
        int versionNumber = Integer.parseInt(versionParts[1]) + 1;
        return versionParts[0] + "_" + String.format("%02d", versionNumber);
      }
    }
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "_01";
  }
}
