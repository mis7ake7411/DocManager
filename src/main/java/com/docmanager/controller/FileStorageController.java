package com.docmanager.controller;

import com.docmanager.constants.FileType;
import com.docmanager.model.vo.FileStorageVO;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.IOException;
import com.docmanager.model.dto.FileStorageReqDTO;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.docmanager.service.fileStorage.FileStorageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fileStorage")
public class FileStorageController {
    private final FileStorageService fileStorageService;


    @PostMapping("/upload")
    public FileStorageVO uploadFile(@ModelAttribute FileStorageReqDTO requestDTO,
                            @RequestParam("file") MultipartFile file) throws IOException {
        // 呼叫 service 儲存檔案與DTO資料
      return fileStorageService.uploadFile(FileType.DOCUMENT, file);
    }

    @GetMapping("/download/{uuid}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String uuid) throws IOException {
        // 由 service 取得檔案內容
        FileStorageVO fileStorageVO = fileStorageService.downloadFile(uuid);
        Path filePath = Paths.get(fileStorageVO.filePath());

        if (!Files.exists(filePath)) {
            throw new IOException("檔案不存在: " + filePath);
        }

        String fileName = fileStorageVO.fullFileName();
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        InputStreamResource resource = new InputStreamResource(Files.newInputStream(filePath));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName)
            .contentType(MediaType.parseMediaType(contentType))
            .contentLength(Files.size(filePath))
            .body(resource);
    }
}
