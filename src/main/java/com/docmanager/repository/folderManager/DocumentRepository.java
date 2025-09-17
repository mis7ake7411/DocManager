package com.docmanager.repository.folderManager;

import com.docmanager.model.entity.Document;
import com.docmanager.repository.BaseRepository;
import java.nio.channels.FileChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface DocumentRepository extends BaseRepository<Document, Long> {
  @NonNull
  @EntityGraph(attributePaths = "file")
  Page<Document> findAll(@NonNull Pageable pageable);

  @Query("""
        SELECT MAX(doc.documentVersion)
            FROM Document doc
        WHERE doc.documentName = :documentName
            AND doc.folder.id = :folderId
            AND doc.deleteFlag = false
    """)
  String findMaxDocumentVersion(String documentName, Long folderId);

  Page<Document> findByFolderId(Long folderId, Pageable pageable);
}