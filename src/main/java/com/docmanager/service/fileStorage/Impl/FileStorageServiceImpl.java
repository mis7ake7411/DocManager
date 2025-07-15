package com.docmanager.service.fileStorage.Impl;

import com.docmanager.provider.FilePathProvider;
import com.docmanager.constants.FileType;
import com.docmanager.model.dto.FileStorageReqDTO.FileNamePartsDTO;
import com.docmanager.model.entity.FileStorage;

import com.docmanager.model.vo.FileStorageVO;
import com.docmanager.repository.folderManager.FileStorageRepository;
import com.docmanager.service.fileStorage.FileStorageService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    private final FilePathProvider pathProvider;
    private final FileStorageRepository fileStorageRepository;


    @Override
    public FileStorageVO uploadFile(FileType fileType, MultipartFile file) throws IOException {
        String originalFileName = getOriginalFileName(file);
        FileNamePartsDTO nameParts = parseFileName(originalFileName);

        UUID fileFolder = UUID.randomUUID();
        Path folderPath = pathProvider.generateFolder(fileType, fileFolder);
        Path filePath = writeFile(file, folderPath, originalFileName);

        FileStorage entity = buildFileStorageEntity(file, fileFolder, filePath, nameParts);
        FileStorage saved = fileStorageRepository.save(entity);

        return FileStorageVO.fromEntity(saved);
    }

    @Override
    public byte[] downloadFile(String filename) throws IOException {
        Path filePath = Paths.get("  ", filename);
        return Files.readAllBytes(filePath);
    }

    /**
     * 獲取原始檔案名稱，並檢查是否為空
     *
     * @param file MultipartFile
     * @return 原始檔案名稱
     */
    private String getOriginalFileName(MultipartFile file) {
        String name = file.getOriginalFilename();
        if (!StringUtils.hasText(name)) throw new IllegalArgumentException("檔案名稱不得為空");
        return name;
    }

    /**
     * 解析檔案名稱，分離出名稱和副檔名
     *
     * @param originalFileName 原始檔案名稱
     * @return 包含名稱和副檔名的 DTO
     */
    private FileNamePartsDTO parseFileName(String originalFileName) {
        int dotIndex = originalFileName.lastIndexOf(".");
        String name = (dotIndex != -1) ? originalFileName.substring(0, dotIndex).trim() : originalFileName.trim();
        String ext = (dotIndex != -1) ? originalFileName.substring(dotIndex + 1).trim() : "";
        return new FileNamePartsDTO(name, ext);
    }

    /**
     * 將檔案寫入指定資料夾
     *
     * @param file MultipartFile
     * @param folder 資料夾路徑
     * @param fileName 檔案名稱
     * @return 寫入後的檔案路徑
     * @throws IOException 如果寫入檔案失敗
     */
    private Path writeFile(MultipartFile file, Path folder, String fileName) throws IOException {
        Path filePath = folder.resolve(fileName);
        file.transferTo(filePath.toFile());
        return filePath;
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
