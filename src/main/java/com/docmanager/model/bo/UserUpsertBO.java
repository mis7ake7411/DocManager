package com.docmanager.model.bo;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserUpsertBO {
  private Long id;
  private String account;
  private String username;
  private String password;
  private Boolean enabled;
  private LocalDateTime createdTime;
  private LocalDateTime modifiedTime;
  private Set<Long> roleIds;
}
