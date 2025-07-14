package com.docmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import lombok.ToString.Exclude;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "folder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Folder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "folder_name" ,nullable = false)
  private String folderName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  @Exclude
  private Folder parent;

  @Column(name = "sort_order")
  private Integer sortOrder;

  @Column(name = "created_time", nullable = false)
  private LocalDateTime createdTime = LocalDateTime.now();

  @Column(name = "modified_time")
  private LocalDateTime modifiedTime;

  @Column(name = "delete_flag", nullable = false)
  private Boolean deleteFlag = false;

  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
  @BatchSize(size = 10)
  @Exclude
  private List<Folder> children;

  @OneToMany(mappedBy = "folder", fetch = FetchType.LAZY)
  @BatchSize(size = 10)
  @Exclude
  private List<Document> documents;

}
