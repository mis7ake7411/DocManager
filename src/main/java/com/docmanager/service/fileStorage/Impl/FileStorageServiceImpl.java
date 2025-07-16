package com.docmanager.service.fileStorage.Impl;

import com.docmanager.component.FileStorageConfigBean;
import com.docmanager.provider.FilePathProvider;
import com.docmanager.constants.FileType;
import com.docmanager.model.dto.FileStorageReqDTO.FileNamePartsDTO;
import com.docmanager.model.entity.FileStorage;

import com.docmanager.model.vo.FileStorageVO;
import com.docmanager.repository.folderManager.FileStorageRepository;
import com.docmanager.service.fileStorage.FileStorageService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    private final FileStorageConfigBean fileStorageConfigBean;
    private final FilePathProvider pathProvider;
    private final FileStorageRepository fileStorageRepository;

    @Override
    public FileStorageVO uploadFile(FileType fileType, MultipartFile file) throws IOException {
        String originalFileName = pathProvider.getOriginalFileName(file);
        FileNamePartsDTO nameParts = pathProvider.parseFileName(originalFileName);

        validateUploadWhiteList(nameParts.extension());

        UUID fileFolder = UUID.randomUUID();
        Path folderPath = pathProvider.generateFolder(fileType, fileFolder);
        Path filePath = pathProvider.writeFile(file, folderPath, originalFileName);

        FileStorage entity = buildFileStorageEntity(file, fileFolder, filePath, nameParts);
        FileStorage saved = fileStorageRepository.save(entity);

        return FileStorageVO.fromEntity(saved);
    }

    @Override
    public FileStorageVO downloadFile(String uuid) {
        FileStorage fileStorage = fileStorageRepository.findByUuid(UUID.fromString(uuid));
        if (fileStorage == null) {
            throw new IllegalArgumentException("檔案不存在或已被刪除");
        }
        // 讀取檔案內容並返回
        return FileStorageVO.fromEntity(fileStorage);
    }

    /**
     * 驗證上傳的檔案副檔名是否在允許的白名單中
     *
     * @param extension 檔案副檔名
     * @throws IllegalArgumentException 如果副檔名不在允許的白名單中
     */
    public void validateUploadWhiteList(String extension) {
        List<String> allowedTypes = Optional.ofNullable(fileStorageConfigBean.getAllowedTypes()).orElse(List.of());
        if (!allowedTypes.contains(extension)) {
            throw new IllegalArgumentException("不允許上傳的檔案類型: " + extension);
        }
    }

    /**
     * 建立 FileStorage 實體，並設置相關屬性
     *
     * @param file MultipartFile
     * @param folder UUID 資料夾 ID
     * @param path 檔案路徑
     * @param parts 檔案名稱和副檔名的 DTO
     * @return FileStorage 實體
     */
    private FileStorage buildFileStorageEntity(MultipartFile file, UUID folder, Path path, FileNamePartsDTO parts) {
        FileStorage entity = new FileStorage();
        entity.setUuid(folder);
        entity.setFileName(parts.name());
        entity.setExtension(parts.extension());
        entity.setFileSize(file.getSize());
        entity.setFilePath(pathProvider.normalizePath(path));
        return entity;
    }
}
