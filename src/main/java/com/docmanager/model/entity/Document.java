package com.docmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import lombok.ToString.Exclude;

@Entity
@Table(name = "document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Document {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "document_name" ,nullable = false)
  private String documentName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "folder_id", nullable = false)
  @Exclude
  private Folder folder;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "file_id")
  @Exclude
  private FileStorage file;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
  @Column(name = "created_time", nullable = false)
  private LocalDateTime createdTime = LocalDateTime.now();

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
  @Column(name = "modified_time")
  private LocalDateTime modifiedTime;

  @Column(name = "delete_flag", nullable = false)
  private Boolean deleteFlag = false;

  @Column(name = "document_version", nullable = false)
  private String documentVersion;

  private String description;

}
