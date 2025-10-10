package com.docmanager.controller;

import com.docmanager.constants.ErrorCode;
import com.docmanager.exception.GlobalException;
import com.docmanager.model.base.ApiResponse;
import com.docmanager.model.dto.UserCreateReqDTO;
import com.docmanager.model.dto.UserUpdateReqDTO;
import com.docmanager.model.entity.Users;
import com.docmanager.model.vo.UserVO;
import com.docmanager.service.user.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

  private final UsersService usersService;

  @PostMapping("/createUser")
  public ApiResponse<UserVO> createUser(@Valid @RequestBody UserCreateReqDTO userCreateReqDTO) {
    return ApiResponse.success(usersService.createUser(userCreateReqDTO));
  }

  @PutMapping("/{id}")
  public ApiResponse<UserVO> updateUser(@PathVariable Long id,
      @Valid @RequestBody UserUpdateReqDTO userUpdateReqDTO) {
    return ApiResponse.success(usersService.updateUser(id, userUpdateReqDTO));
  }

  @PostMapping("/{id}/lock")
  public ApiResponse<Void> lockUser(@PathVariable Long id) {
    usersService.lockUser(id);
    return ApiResponse.success();
  }

  @PostMapping("/{id}/unlock")
  public ApiResponse<Void> unlockUser(@PathVariable Long id) {
    usersService.unlockUser(id);
    return ApiResponse.success();
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> deleteUser(@PathVariable Long id) {
    usersService.deleteUser(id);
    return ApiResponse.success();
  }

  @GetMapping("/{id}")
  public ApiResponse<Users> getUser(@PathVariable Long id) {
    return usersService.getUserById(id)
        .map(ApiResponse::success)
        .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND,
            ErrorCode.VALIDATION_ERROR.message()));
  }

  @GetMapping
  public ApiResponse<List<Users>> getAllUsers() {
    return ApiResponse.success(usersService.getAllUsers());
  }
}
