package com.docmanager.repository.folderManager;

import com.docmanager.model.entity.Document;
import com.docmanager.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocumentRepository extends BaseRepository<Document, Long> {

  @Query("""
        SELECT MAX(doc.documentVersion)
            FROM Document doc
        WHERE doc.documentName = :documentName
            AND doc.folder.id = :categoryID
            AND doc.deleteFlag = false
    """)
  String findMaxDocumentVersion(String documentName, Long folderId);
}