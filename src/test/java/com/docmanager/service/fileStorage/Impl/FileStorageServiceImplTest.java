package com.docmanager.service.fileStorage.Impl;

import com.docmanager.component.FileStorageConfigBean;
import com.docmanager.constants.FileType;
import com.docmanager.model.dto.FileStorageReqDTO.FileNamePartsDTO;
import com.docmanager.model.entity.FileStorage;
import com.docmanager.model.vo.FileStorageVO;
import com.docmanager.provider.FilePathProvider;
import com.docmanager.repository.folderManager.FileStorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileStorageServiceImplTest {

    @Mock
    private FileStorageConfigBean config;
    @Mock
    private FilePathProvider pathProvider;
    @Mock
    private FileStorageRepository fileStorageRepository;
    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileStorageServiceImpl service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("uploadFile 成功：應驗證欄位並回傳 VO")
    void uploadFile_success() throws IOException {
        when(config.getMaxUploadSize()).thenReturn(DataSize.ofMegabytes(5));
        when(config.getAllowedTypes()).thenReturn(List.of("txt", "pdf"));

        when(multipartFile.getSize()).thenReturn(1024L);
        when(multipartFile.getOriginalFilename()).thenReturn("readme.txt");

        when(pathProvider.getOriginalFileName(multipartFile)).thenReturn("readme.txt");
        when(pathProvider.parseFileName("readme.txt")).thenReturn(new FileNamePartsDTO("readme", "txt"));
        Path folder = Path.of("upload/test");
        Path filePath = folder.resolve("readme.txt");
        when(pathProvider.generateFolder(eq(FileType.DOCUMENT), any(UUID.class))).thenReturn(folder);
        when(pathProvider.writeFile(multipartFile, folder, "readme.txt")).thenReturn(filePath);
        when(pathProvider.normalizePath(filePath)).thenReturn(filePath.toString().replace("\\", "/"));

        when(fileStorageRepository.save(any(FileStorage.class))).thenAnswer(inv -> {
            FileStorage fs = inv.getArgument(0);
            fs.setId(10L);
            return fs;
        });

        FileStorageVO vo = service.uploadFile(FileType.DOCUMENT, multipartFile);

        assertNotNull(vo);
        assertEquals("readme", vo.fileName());
        assertEquals("txt", vo.extension());
        assertTrue(vo.fullFileName().endsWith("readme.txt"));

        ArgumentCaptor<FileStorage> captor = ArgumentCaptor.forClass(FileStorage.class);
        verify(fileStorageRepository).save(captor.capture());
        FileStorage saved = captor.getValue();
        assertNotNull(saved.getUuid());
        assertEquals("readme", saved.getFileName());
        assertEquals("txt", saved.getExtension());
        assertEquals(1024L, saved.getFileSize());
    }

    @Test
    @DisplayName("uploadFile 檔案大小超限應拋出異常")
    void uploadFile_sizeExceed() {
        when(config.getMaxUploadSize()).thenReturn(DataSize.ofKilobytes(1)); // 1KB
        when(multipartFile.getSize()).thenReturn(4096L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> service.uploadFile(FileType.IMAGE, multipartFile));
        assertTrue(ex.getMessage().contains("檔案超過限制"));
        verify(fileStorageRepository, never()).save(any());
    }

    @Test
    @DisplayName("uploadFile 副檔名不允許應拋出異常")
    void uploadFile_extensionNotAllowed() throws IOException {
        when(config.getMaxUploadSize()).thenReturn(DataSize.ofMegabytes(5));
        when(config.getAllowedTypes()).thenReturn(List.of("txt", "pdf"));

        when(multipartFile.getSize()).thenReturn(100L);
        when(multipartFile.getOriginalFilename()).thenReturn("malware.exe");
        when(pathProvider.getOriginalFileName(multipartFile)).thenReturn("malware.exe");
        when(pathProvider.parseFileName("malware.exe")).thenReturn(new FileNamePartsDTO("malware", "exe"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> service.uploadFile(FileType.OTHER, multipartFile));
        assertTrue(ex.getMessage().contains("不允許上傳的檔案類型"));
        verify(fileStorageRepository, never()).save(any());
    }

    @Test
    @DisplayName("downloadFile 成功取得檔案資料")
    void downloadFile_success() {
        UUID uuid = UUID.randomUUID();
        FileStorage fs = FileStorage.builder()
            .id(20L)
            .uuid(uuid)
            .fileName("pic")
            .extension("jpg")
            .filePath("/data/pic.jpg")
            .fileSize(2048L)
            .uploadTime(LocalDateTime.now())
            .build();
        when(fileStorageRepository.findByUuid(uuid)).thenReturn(fs);

        FileStorageVO vo = service.downloadFile(uuid.toString());
        assertEquals("pic", vo.fileName());
        assertEquals("jpg", vo.extension());
        assertEquals(2048L, vo.fileSize());
    }

    @Test
    @DisplayName("downloadFile uuid 不存在應拋出異常")
    void downloadFile_notFound() {
        UUID uuid = UUID.randomUUID();
        when(fileStorageRepository.findByUuid(uuid)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> service.downloadFile(uuid.toString()));
    }
}

