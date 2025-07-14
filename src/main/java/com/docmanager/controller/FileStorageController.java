package com.docmanager.controller;

import lombok.RequiredArgsConstructor;
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
import org.springframework.core.io.ByteArrayResource;
import com.docmanager.model.dto.FileStorageReqDTO;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.docmanager.service.fileStorage.FileStorageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fileStorage")
public class FileStorageController {
    private final FileStorageService fileStorageService;


    @PostMapping("/upload")
    public String uploadFile(@ModelAttribute FileStorageReqDTO requestDTO,
                            @RequestParam("file") MultipartFile file) throws IOException {
        // 呼叫 service 儲存檔案與DTO資料
        fileStorageService.uploadFile(requestDTO, file);
        return "檔案上傳成功: " + file.getOriginalFilename();
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("filename") String filename) throws IOException {
        // 由 service 取得檔案內容
        byte[] fileContent = fileStorageService.downloadFile(filename);
        ByteArrayResource resource = new ByteArrayResource(fileContent);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(fileContent.length)
                .body(resource);
    }
}
