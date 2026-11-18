package com.xiaofeng.lostfound.notification;
import com.xiaofeng.lostfound.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);
    long countByRecipientAndReadFlagFalse(User recipient);
}
