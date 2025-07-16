package com.docmanager.provider;

import com.docmanager.component.FileStorageConfigBean;
import com.docmanager.constants.FileType;
import com.docmanager.model.dto.FileStorageReqDTO.FileNamePartsDTO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FilePathProvider {

  private final FileStorageConfigBean fileStorageConfigBean;

  public Path getBasePath() {
    return Paths.get(fileStorageConfigBean.getUploadPath());
  }

  /**
   * 根據檔案類型和UUID生成對應的資料夾路徑
   *
   * @param fileType   檔案類型
   * @param folderUUID 資料夾UUID
   * @return 資料夾路徑
   * @throws IOException 如果無法創建資料夾
   */
  public Path generateFolder(FileType fileType, UUID folderUUID) throws IOException {
    Path folderPath = getBasePath()
        .resolve(fileType.getFolderName())
        .resolve(folderUUID.toString());
    Files.createDirectories(folderPath);
    return folderPath;
  }

  /**
   * 正規化路徑，將反斜線替換為斜線
   *
   * @param path 要正規化的路徑
   * @return 正規化後的路徑字串
   */
  public String normalizePath(Path path) {
    return path.toAbsolutePath().normalize().toString().replace("\\", "/");
  }

  /**
   * 獲取原始檔案名稱，並檢查是否為空
   *
   * @param file MultipartFile
   * @return 原始檔案名稱
   */
  public String getOriginalFileName(MultipartFile file) {
    String name = file.getOriginalFilename();
    if (!StringUtils.hasText(name)) {
      throw new IllegalArgumentException("檔案名稱不得為空");
    }
    return name;
  }

  /**
   * 解析檔案名稱，分離出名稱和副檔名
   *
   * @param originalFileName 原始檔案名稱
   * @return 包含名稱和副檔名的 DTO
   */
  public FileNamePartsDTO parseFileName(String originalFileName) {
    int dotIndex = originalFileName.lastIndexOf(".");
    String name =
        (dotIndex != -1) ? originalFileName.substring(0, dotIndex).trim() : originalFileName.trim();
    String ext =
        (dotIndex != -1) ? originalFileName.substring(dotIndex + 1).trim().toLowerCase() : "";
    return new FileNamePartsDTO(name, ext);
  }

  /**
   * 將檔案寫入指定資料夾
   *
   * @param file     MultipartFile
   * @param folder   資料夾路徑
   * @param fileName 檔案名稱
   * @return 寫入後的檔案路徑
   * @throws IOException 如果寫入檔案失敗
   */
  public Path writeFile(MultipartFile file, Path folder, String fileName) throws IOException {
    Path filePath = folder.resolve(fileName);
    file.transferTo(filePath.toFile());
    return filePath;
  }
}
