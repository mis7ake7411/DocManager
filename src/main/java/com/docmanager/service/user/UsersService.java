package com.docmanager.service.user;

import com.docmanager.model.dto.UserCreateReqDTO;
import com.docmanager.model.dto.UserUpdateReqDTO;
import com.docmanager.model.entity.Users;
import com.docmanager.model.vo.UserVO;
import java.util.Optional;
import java.util.List;

public interface UsersService {
    UserVO createUser(UserCreateReqDTO userCreateReqDTO);
    UserVO updateUser(Long id, UserUpdateReqDTO userUpdateReqDTO);
    void lockUser(Long id);
    void unlockUser(Long id);
    void deleteUser(Long id);
    Optional<Users> getUserById(Long id);
    List<Users> getAllUsers();
}
