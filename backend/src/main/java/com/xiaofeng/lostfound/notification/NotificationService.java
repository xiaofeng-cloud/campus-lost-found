package com.xiaofeng.lostfound.notification;
import com.xiaofeng.lostfound.common.BusinessException;
import com.xiaofeng.lostfound.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository repository;
    public NotificationService(NotificationRepository repository) { this.repository = repository; }
    @Transactional public void send(User user, String title, String content) { repository.save(new Notification(user, title, content)); }
    @Transactional(readOnly = true) public List<NotificationView> list(User user) { return repository.findByRecipientOrderByCreatedAtDesc(user).stream().map(NotificationView::from).toList(); }
    @Transactional public void read(User user, Long id) {
        Notification n = repository.findById(id).orElseThrow(() -> new BusinessException("通知不存在"));
        if (!n.getRecipient().getId().equals(user.getId())) throw new BusinessException("不能操作他人的通知");
        n.markRead();
    }
    public record NotificationView(Long id, String title, String content, boolean read, LocalDateTime createdAt) {
        static NotificationView from(Notification n) { return new NotificationView(n.getId(), n.getTitle(), n.getContent(), n.isReadFlag(), n.getCreatedAt()); }
    }
}
