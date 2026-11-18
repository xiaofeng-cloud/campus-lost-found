package com.xiaofeng.lostfound.claim.dto;
import com.xiaofeng.lostfound.claim.ClaimApplication;
import java.time.LocalDateTime;
public record ClaimView(Long id, Long itemId, String itemTitle, Long applicantId, String applicantName, String proof,
                        String status, String handleComment, LocalDateTime createdAt) {
    public static ClaimView from(ClaimApplication c) { return new ClaimView(c.getId(), c.getItem().getId(), c.getItem().getTitle(),
            c.getApplicant().getId(), c.getApplicant().getNickname(), c.getProof(), c.getStatus().name(), c.getHandleComment(), c.getCreatedAt()); }
}
