package com.xiaofeng.lostfound.item;

import com.xiaofeng.lostfound.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemPost, Long>, JpaSpecificationExecutor<ItemPost> {
    List<ItemPost> findByPublisherOrderByCreatedAtDesc(User publisher);
    List<ItemPost> findByStatusOrderByCreatedAtDesc(ItemStatus status);
    long countByStatus(ItemStatus status);
}
