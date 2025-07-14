package com.docmanager.service.fileStorage;

import com.docmanager.model.dto.FileStorageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    void uploadFile(FileStorageReqDTO requestDTO, MultipartFile file) throws IOException;

    byte[] downloadFile(String filename) throws IOException;
}
