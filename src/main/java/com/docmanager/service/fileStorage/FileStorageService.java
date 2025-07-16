package com.docmanager.service.fileStorage;

import com.docmanager.constants.FileType;
import com.docmanager.model.vo.FileStorageVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    FileStorageVO uploadFile(FileType fileType, MultipartFile file) throws IOException;

    FileStorageVO downloadFile(String uuid) throws IOException;
}
