package com.xiaofeng.lostfound.auth;

import com.xiaofeng.lostfound.auth.dto.*;
import com.xiaofeng.lostfound.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<UserView> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.ok("注册成功", authService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok("登录成功", authService.login(request));
    }
}
