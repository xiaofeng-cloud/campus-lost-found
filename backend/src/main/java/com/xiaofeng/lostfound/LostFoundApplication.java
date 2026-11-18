package com.xiaofeng.lostfound;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class LostFoundApplication {
    public static void main(String[] args) {
        SpringApplication.run(LostFoundApplication.class, args);
    }
}
