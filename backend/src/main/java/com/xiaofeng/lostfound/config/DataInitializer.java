package com.xiaofeng.lostfound.config;
import com.xiaofeng.lostfound.user.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository users; private final PasswordEncoder encoder; private final String password;
    public DataInitializer(UserRepository users, PasswordEncoder encoder, @Value("${app.admin.initial-password:Admin@123456}") String password) { this.users = users; this.encoder = encoder; this.password = password; }
    @Override public void run(String... args) {
        if (!users.existsByUsername("admin")) users.save(new User("admin", "系统管理员", encoder.encode(password), "", Role.ADMIN));
    }
}
