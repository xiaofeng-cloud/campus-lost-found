package com.xiaofeng.lostfound.item;

import com.xiaofeng.lostfound.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "item_post", indexes = {
        @Index(name = "idx_item_status_created", columnList = "status,createdAt"),
        @Index(name = "idx_item_type_category", columnList = "postType,category")
})
public class ItemPost {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private PostType postType;
    @Column(nullable = false, length = 120)
    private String title;
    @Column(nullable = false, length = 2000)
    private String description;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private ItemCategory category;
    @Column(nullable = false, length = 160)
    private String location;
    @Column(nullable = false)
    private LocalDateTime occurredAt;
    @Column(nullable = false, length = 100)
    private String contact;
    @Column(length = 500)
    private String imageUrl;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private ItemStatus status;
    @Column(length = 500)
    private String reviewComment;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publisher_id", nullable = false)
    private User publisher;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected ItemPost() {}

    public ItemPost(User publisher, PostType postType, String title, String description, ItemCategory category,
                    String location, LocalDateTime occurredAt, String contact, String imageUrl) {
        this.publisher = publisher;
        this.postType = postType;
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.occurredAt = occurredAt;
        this.contact = contact;
        this.imageUrl = imageUrl;
        this.status = ItemStatus.PENDING_REVIEW;
    }

    @PrePersist void createTime() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate void updateTime() { updatedAt = LocalDateTime.now(); }

    public void review(boolean approved, String comment) {
        status = approved ? ItemStatus.SEARCHING : ItemStatus.REJECTED;
        reviewComment = comment;
    }
    public void markClaiming() { status = ItemStatus.CLAIMING; }
    public void markFound() { status = ItemStatus.FOUND; }
    public void close() { status = ItemStatus.CLOSED; }
    public void update(PostType postType, String title, String description, ItemCategory category, String location,
                       LocalDateTime occurredAt, String contact, String imageUrl) {
        this.postType = postType; this.title = title; this.description = description; this.category = category;
        this.location = location; this.occurredAt = occurredAt; this.contact = contact; this.imageUrl = imageUrl;
        this.status = ItemStatus.PENDING_REVIEW; this.reviewComment = null;
    }

    public Long getId() { return id; }
    public PostType getPostType() { return postType; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public ItemCategory getCategory() { return category; }
    public String getLocation() { return location; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    public String getContact() { return contact; }
    public String getImageUrl() { return imageUrl; }
    public ItemStatus getStatus() { return status; }
    public String getReviewComment() { return reviewComment; }
    public User getPublisher() { return publisher; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
