package com.docmanager.provider;

import com.docmanager.component.FileStorageConfigBean;
import com.docmanager.constants.FileType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
   * @param fileType 檔案類型
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
   * @param path 要正規化的路徑
   * @return 正規化後的路徑字串
   */
  public String normalizePath(Path path) {
    return path.toAbsolutePath().normalize().toString().replace("\\", "/");
  }
}
