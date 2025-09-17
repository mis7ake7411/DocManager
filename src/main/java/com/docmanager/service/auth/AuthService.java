package com.docmanager.service.auth;

import com.docmanager.model.dto.LoginReqDTO;
import com.docmanager.model.dto.LoginRespDTO;
import com.docmanager.model.dto.RefreshReqDTO;
import com.docmanager.model.dto.RegisterReqDTO;

public interface AuthService {
    LoginRespDTO login(LoginReqDTO loginRequest);
    LoginRespDTO register(RegisterReqDTO registerRequest);
    LoginRespDTO refreshToken(RefreshReqDTO refreshToken);
    void logout(String account);

}
