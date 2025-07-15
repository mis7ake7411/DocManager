package com.docmanager.service.fileStorage;

import com.docmanager.model.dto.FileStorageReqDTO;
import com.docmanager.model.vo.FileStorageVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    FileStorageVO uploadFile(FileStorageReqDTO requestDTO, MultipartFile file) throws IOException;

    byte[] downloadFile(String filename) throws IOException;
}
