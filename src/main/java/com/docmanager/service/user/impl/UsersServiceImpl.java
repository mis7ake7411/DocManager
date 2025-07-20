package com.docmanager.service.user.impl;

import com.docmanager.constants.ErrorCode;
import com.docmanager.exception.GlobalException;
import com.docmanager.model.bo.UserUpsertBO;
import com.docmanager.model.dto.UserCreateReqDTO;
import com.docmanager.model.dto.UserUpdateReqDTO;
import com.docmanager.model.entity.Roles;
import com.docmanager.model.entity.Users;
import com.docmanager.model.vo.UserVO;
import com.docmanager.repository.users.RolesRepository;
import com.docmanager.repository.users.UsersRepository;
import com.docmanager.service.user.UsersService;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserVO createUser(UserCreateReqDTO userCreateReqDTO) {
        UserUpsertBO bo = userCreateReqDTO.toBO();
        usersRepository.findByAccount(bo.getAccount())
            .ifPresent(user -> {
                throw new GlobalException(ErrorCode.USER_EXISTS);
            });

        Users user = new Users();
        user.setUsername(bo.getUsername());
        user.setAccount(bo.getAccount());
        user.setPassword(passwordEncoder.encode(bo.getPassword()));
        user.setEnabled(bo.getEnabled());
        user.setCreatedTime(LocalDateTime.now());

        List<Roles> roles = rolesRepository.findByIdIn(bo.getRoleIds());
        user.setRoles(roles);
        Users savedUser = usersRepository.save(user);
        return UserVO.fromEntity(savedUser);
    }

    @Override
    public UserVO updateUser(Long id, UserUpdateReqDTO userUpdateReqDTO) {
        UserUpsertBO bo = userUpdateReqDTO.toBO();
        Users existingUser = usersRepository.findById(id)
            .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        existingUser.setUsername(bo.getUsername());
        existingUser.setAccount(bo.getAccount());
        existingUser.setModifiedTime(LocalDateTime.now());
        existingUser.setEnabled(Objects.nonNull(bo.getEnabled())
            ? bo.getEnabled() : existingUser.getEnabled());
        List<Roles> roles;
        if (CollectionUtils.isNotEmpty(bo.getRoleIds())) {
            roles = rolesRepository.findByIdIn(bo.getRoleIds());
        } else {
            roles = existingUser.getRoles();
        }
        existingUser.setRoles(roles);
        Users updatedUser = usersRepository.save(existingUser);
        return UserVO.fromEntity(updatedUser);
    }

    @Override
    public void lockUser(Long id) {
        Users user = usersRepository.findById(id)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "使用者不存在"));
        user.setEnabled(true);
        usersRepository.save(user);
    }

    @Override
    public void unlockUser(Long id) {
        Users user = usersRepository.findById(id)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "使用者不存在"));
        user.setEnabled(false);
        usersRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }

    @Override
    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }
}

