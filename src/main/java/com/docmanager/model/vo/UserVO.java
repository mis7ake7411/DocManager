package com.docmanager.model.vo;

import com.docmanager.model.entity.Roles;
import com.docmanager.model.entity.Users;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String account;
    private Boolean enabled;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private List<String> roles;

    public static UserVO fromEntity(Users user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setAccount(user.getAccount());
        vo.setEnabled(user.getEnabled());
        vo.setCreatedTime(user.getCreatedTime());
        vo.setModifiedTime(user.getModifiedTime());
        if (user.getRoles() != null) {
            vo.setRoles(user.getRoles().stream().map(Roles::getRoleName).collect(Collectors.toList()));
        }
        return vo;
    }
}

