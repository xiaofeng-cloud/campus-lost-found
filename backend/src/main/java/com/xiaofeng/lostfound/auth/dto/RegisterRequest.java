package com.xiaofeng.lostfound.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "账号不能为空") @Size(max = 40, message = "账号不能超过40个字符") String username,
        @NotBlank(message = "昵称不能为空") @Size(max = 80, message = "昵称不能超过80个字符") String nickname,
        @NotBlank(message = "密码不能为空") @Size(min = 8, max = 64, message = "密码长度应为8至64位") String password,
        @Pattern(regexp = "^$|^1\\d{10}$", message = "手机号格式不正确") String phone
) {}
