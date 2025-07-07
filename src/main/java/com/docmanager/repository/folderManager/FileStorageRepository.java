package com.docmanager.repository.folderManager;

import com.docmanager.model.entity.FileStorage;
import com.docmanager.repository.BaseRepository;
import java.util.UUID;

public interface FileStorageRepository extends BaseRepository<FileStorage, Long> {
  FileStorage findByUuid(UUID uuid);
}