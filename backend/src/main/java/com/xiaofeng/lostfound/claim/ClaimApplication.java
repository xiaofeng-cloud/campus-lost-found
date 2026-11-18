package com.xiaofeng.lostfound.claim;
import com.xiaofeng.lostfound.item.ItemPost;
import com.xiaofeng.lostfound.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "claim_application")
public class ClaimApplication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "item_id") private ItemPost item;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "applicant_id") private User applicant;
    @Column(nullable = false, length = 1000) private String proof;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private ClaimStatus status = ClaimStatus.PENDING;
    @Column(length = 500) private String handleComment;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    private LocalDateTime handledAt;
    protected ClaimApplication() {}
    public ClaimApplication(ItemPost item, User applicant, String proof) { this.item = item; this.applicant = applicant; this.proof = proof; }
    @PrePersist void createTime() { createdAt = LocalDateTime.now(); }
    public void process(boolean approved, String comment) { status = approved ? ClaimStatus.APPROVED : ClaimStatus.REJECTED; handleComment = comment; handledAt = LocalDateTime.now(); }
    public void complete() { status = ClaimStatus.COMPLETED; handledAt = LocalDateTime.now(); }
    public Long getId() { return id; } public ItemPost getItem() { return item; } public User getApplicant() { return applicant; }
    public String getProof() { return proof; } public ClaimStatus getStatus() { return status; } public String getHandleComment() { return handleComment; }
    public LocalDateTime getCreatedAt() { return createdAt; } public LocalDateTime getHandledAt() { return handledAt; }
}
