package com.docmanager.model.entity;

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

  @Column(name = "created_time", nullable = false)
  private LocalDateTime createdTime = LocalDateTime.now();

  @Column(name = "modified_time")
  private LocalDateTime modifiedTime;

  @Column(name = "delete_flag", nullable = false)
  private Boolean deleteFlag = false;

  @Column(name = "document_version", nullable = false)
  private String documentVersion;

  private String description;

}
