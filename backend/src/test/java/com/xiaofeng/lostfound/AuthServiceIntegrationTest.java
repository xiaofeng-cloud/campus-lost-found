package com.xiaofeng.lostfound;

import com.xiaofeng.lostfound.auth.AuthService;
import com.xiaofeng.lostfound.auth.dto.LoginRequest;
import com.xiaofeng.lostfound.auth.dto.RegisterRequest;
import com.xiaofeng.lostfound.common.BusinessException;
import com.xiaofeng.lostfound.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 注册后应使用加密密码保存普通用户() {
        authService.register(new RegisterRequest("20260001", "张同学", "abc12345", "13800000001"));

        var saved = userRepository.findByUsername("20260001").orElseThrow();
        assertThat(saved.getNickname()).isEqualTo("张同学");
        assertThat(saved.getPassword()).isNotEqualTo("abc12345");
        assertThat(saved.getRole().name()).isEqualTo("USER");
    }

    @Test
    void 重复账号注册应给出中文提示() {
        var request = new RegisterRequest("20260002", "李同学", "abc12345", "13800000002");
        authService.register(request);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("账号已存在");
    }

    @Test
    void 账号首尾空格不能绕过重复检查() {
        authService.register(new RegisterRequest("student01", "学生一", "abc12345", "13800000021"));

        assertThatThrownBy(() -> authService.register(new RegisterRequest(" student01 ", "学生二", "abc12345", "13800000022")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("账号已存在");
    }

    @Test
    void 正确账号密码登录应返回令牌和用户信息() {
        authService.register(new RegisterRequest("20260003", "王同学", "abc12345", "13800000003"));

        var response = authService.login(new LoginRequest("20260003", "abc12345"));

        assertThat(response.token()).isNotBlank();
        assertThat(response.user().nickname()).isEqualTo("王同学");
        assertThat(response.user().role()).isEqualTo("USER");
    }
}
