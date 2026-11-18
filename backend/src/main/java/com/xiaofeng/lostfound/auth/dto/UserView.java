package com.xiaofeng.lostfound.auth.dto;

import com.xiaofeng.lostfound.user.User;

public record UserView(Long id, String username, String nickname, String phone, String role) {
    public static UserView from(User user) {
        return new UserView(user.getId(), user.getUsername(), user.getNickname(), user.getPhone(), user.getRole().name());
    }
}
