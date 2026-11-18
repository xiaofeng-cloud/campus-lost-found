package com.xiaofeng.lostfound.auth;

import com.xiaofeng.lostfound.auth.dto.*;
import com.xiaofeng.lostfound.common.BusinessException;
import com.xiaofeng.lostfound.user.Role;
import com.xiaofeng.lostfound.user.User;
import com.xiaofeng.lostfound.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public UserView register(RegisterRequest request) {
        String username = request.username().trim();
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("账号已存在");
        }
        User user = new User(username, request.nickname().trim(),
                passwordEncoder.encode(request.password()), request.phone(), Role.USER);
        return UserView.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .filter(User::isEnabled)
                .orElseThrow(() -> new BusinessException("账号或密码错误"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }
        return new LoginResponse(jwtService.generate(user), UserView.from(user));
    }

    @Transactional(readOnly = true)
    public User currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("登录状态已失效"));
    }
}
