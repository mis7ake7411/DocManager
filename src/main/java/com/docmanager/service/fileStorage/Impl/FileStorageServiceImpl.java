package com.docmanager.service.fileStorage.Impl;

import com.docmanager.model.bo.FileStorageBO;
import com.docmanager.model.dto.FileStorageReqDTO;
import com.docmanager.model.entity.FileStorage;

import com.docmanager.repository.folderManager.FileStorageRepository;
import com.docmanager.service.fileStorage.FileStorageService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    private static final String UPLOAD_DIR = "uploads";
    private final FileStorageRepository fileStorageRepository;


    @Override
    public void uploadFile(FileStorageReqDTO requestDTO, MultipartFile file) throws IOException {
        FileStorageBO bo = requestDTO.toBO();
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String originalFileName = file.getOriginalFilename();
        String extension = null;
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
        }
        Path filePath = uploadPath.resolve(originalFileName);
        file.transferTo(filePath.toFile());
        // 將BO轉為Entity並存入DB
        FileStorage entity = new FileStorage();
        entity.setUuid(UUID.randomUUID());
        entity.setFileName(bo.getFileName());
        entity.setFileSize(bo.getFileSize());
        entity.setFilePath(filePath.toString());
        entity.setExtension(extension);
        fileStorageRepository.save(entity);
    }

    @Override
    public byte[] downloadFile(String filename) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR, filename);
        return Files.readAllBytes(filePath);
    }
}
