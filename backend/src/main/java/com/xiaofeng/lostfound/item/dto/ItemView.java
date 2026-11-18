package com.xiaofeng.lostfound.item.dto;

import com.xiaofeng.lostfound.item.ItemPost;

import java.time.LocalDateTime;

public record ItemView(Long id, String postType, String title, String description, String category,
                       String location, LocalDateTime occurredAt, String contact, String imageUrl,
                       String status, String reviewComment, Long publisherId, String publisherName,
                       LocalDateTime createdAt) {
    public static ItemView from(ItemPost item) {
        return new ItemView(item.getId(), item.getPostType().name(), item.getTitle(), item.getDescription(),
                item.getCategory().name(), item.getLocation(), item.getOccurredAt(), item.getContact(),
                item.getImageUrl(), item.getStatus().name(), item.getReviewComment(), item.getPublisher().getId(),
                item.getPublisher().getNickname(), item.getCreatedAt());
    }
}
