package com.docmanager.service.auth.impl;

import com.docmanager.constants.ErrorCode;
import com.docmanager.exception.GlobalException;
import com.docmanager.model.dto.LoginReqDTO;
import com.docmanager.model.dto.LoginRespDTO;
import com.docmanager.model.dto.RefreshReqDTO;
import com.docmanager.model.dto.RegisterReqDTO;
import com.docmanager.model.entity.Roles;
import com.docmanager.model.entity.Users;
import com.docmanager.repository.users.RolesRepository;
import com.docmanager.repository.users.UsersRepository;
import com.docmanager.security.JwtUtil;
import com.docmanager.service.auth.AuthService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginRespDTO login(LoginReqDTO loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.account(), loginRequest.password())
        );
        Users user = usersRepository.findByAccount(loginRequest.account())
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        List<String> roles = user.getRoles().stream().map(Roles::getRoleName).toList();
        return new LoginRespDTO(accessToken, refreshToken, user.getUsername(), roles);
    }

    @Override
    public LoginRespDTO register(RegisterReqDTO registerRequest) {
        Users existingUser = usersRepository.findByAccount(registerRequest.account())
                .orElse(null);
        if (existingUser != null) {
            throw new GlobalException(ErrorCode.USER_EXISTS);
        }

        Users newUser = new Users();
        newUser.setUsername(registerRequest.username());
        newUser.setAccount(registerRequest.account());
        newUser.setPassword(passwordEncoder.encode(registerRequest.password()));
        newUser.setEnabled(true); // 預設啟用
        List<Roles> defaultRole = rolesRepository.findByIdIn(Set.of(1L, 2L));
        newUser.setRoles(defaultRole);

        Users savedUser = usersRepository.save(newUser);
        String accessToken = jwtUtil.generateAccessToken(savedUser);
        String refreshToken = jwtUtil.generateRefreshToken(savedUser);
        List<String> roles = savedUser.getRoles().stream().map(Roles::getRoleName).toList();
        return new LoginRespDTO(accessToken, refreshToken, savedUser.getUsername(), roles);
    }

    @Override
    public LoginRespDTO refreshToken(RefreshReqDTO refresh) {
        String username;
        try {
            username = jwtUtil.extractAccountFromRefresh(refresh.refreshToken());
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.message());
        }
        Users user = usersRepository.findByAccount(username)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
        String newAccessToken = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);
        List<String> roles = user.getRoles().stream().map(Roles::getRoleName).toList();
        return new LoginRespDTO(newAccessToken, newRefreshToken, user.getUsername(), roles);
    }

    @Override
    public void logout(String account) {
        // 若有 refresh token 黑名單或快取，可在此處理
        // 目前僅為佔位，實際可依需求擴充
    }
}
