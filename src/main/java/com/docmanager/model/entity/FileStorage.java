package com.docmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "file_storage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FileStorage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private UUID uuid;

  @Column(name = "file_path", nullable = false)
  private String filePath;

  @Column(name = "file_name")
  private String fileName;

  private String extension;

  @Column(name = "file_size")
  private Long fileSize;

  @Column(name = "upload_time")
  private LocalDateTime uploadTime;

}
