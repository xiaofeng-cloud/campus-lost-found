package com.xiaofeng.lostfound.notification;

import com.xiaofeng.lostfound.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "recipient_id") private User recipient;
    @Column(nullable = false, length = 120) private String title;
    @Column(nullable = false, length = 1000) private String content;
    @Column(nullable = false) private boolean readFlag;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    protected Notification() {}
    public Notification(User recipient, String title, String content) { this.recipient = recipient; this.title = title; this.content = content; }
    @PrePersist void createTime() { createdAt = LocalDateTime.now(); }
    public void markRead() { readFlag = true; }
    public Long getId() { return id; } public User getRecipient() { return recipient; } public String getTitle() { return title; }
    public String getContent() { return content; } public boolean isReadFlag() { return readFlag; } public LocalDateTime getCreatedAt() { return createdAt; }
}
