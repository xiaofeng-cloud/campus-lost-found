package com.xiaofeng.lostfound.user;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sys_user", uniqueConstraints = @UniqueConstraint(name = "uk_user_username", columnNames = "username"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String username;

    @Column(nullable = false, length = 80)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected User() {
    }

    public User(String username, String nickname, String password, String phone, Role role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getNickname() { return nickname; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public Role getRole() { return role; }
    public boolean isEnabled() { return enabled; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
