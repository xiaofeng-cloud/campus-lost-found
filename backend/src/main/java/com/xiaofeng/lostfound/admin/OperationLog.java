package com.xiaofeng.lostfound.admin;
import com.xiaofeng.lostfound.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "operation_log")
public class OperationLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "operator_id") private User operator;
    @Column(nullable = false, length = 60) private String action;
    @Column(nullable = false, length = 60) private String targetType;
    @Column(nullable = false) private Long targetId;
    @Column(nullable = false, length = 500) private String detail;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    protected OperationLog() {}
    public OperationLog(User operator, String action, String targetType, Long targetId, String detail) { this.operator = operator; this.action = action; this.targetType = targetType; this.targetId = targetId; this.detail = detail; }
    @PrePersist void createTime() { createdAt = LocalDateTime.now(); }
    public Long getId() { return id; } public User getOperator() { return operator; } public String getAction() { return action; }
    public String getTargetType() { return targetType; } public Long getTargetId() { return targetId; } public String getDetail() { return detail; } public LocalDateTime getCreatedAt() { return createdAt; }
}
