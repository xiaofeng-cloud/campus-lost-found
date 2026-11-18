package com.xiaofeng.lostfound.item;

import com.xiaofeng.lostfound.common.BusinessException;
import com.xiaofeng.lostfound.item.dto.ItemRequest;
import com.xiaofeng.lostfound.item.dto.ItemView;
import com.xiaofeng.lostfound.user.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) { this.itemRepository = itemRepository; }

    @Transactional
    public ItemView create(User publisher, ItemRequest request) {
        ItemPost item = new ItemPost(publisher, request.postType(), request.title().trim(), request.description().trim(),
                request.category(), request.location().trim(), request.occurredAt(), request.contact().trim(), request.imageUrl());
        return ItemView.from(itemRepository.save(item));
    }

    @Transactional
    public ItemView update(User user, Long id, ItemRequest request) {
        ItemPost item = require(id);
        requireOwner(user, item);
        if (item.getStatus() != ItemStatus.PENDING_REVIEW && item.getStatus() != ItemStatus.REJECTED) {
            throw new BusinessException("当前状态不能编辑");
        }
        item.update(request.postType(), request.title().trim(), request.description().trim(), request.category(),
                request.location().trim(), request.occurredAt(), request.contact().trim(), request.imageUrl());
        return ItemView.from(item);
    }

    @Transactional(readOnly = true)
    public Page<ItemView> publicList(String keyword, PostType type, ItemCategory category, int page, int size) {
        Specification<ItemPost> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(root.get("status").in(ItemStatus.SEARCHING, ItemStatus.CLAIMING, ItemStatus.FOUND));
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("description")), like), cb.like(cb.lower(root.get("location")), like)));
            }
            if (type != null) predicates.add(cb.equal(root.get("postType"), type));
            if (category != null) predicates.add(cb.equal(root.get("category"), category));
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50), Sort.by("createdAt").descending());
        return itemRepository.findAll(spec, pageable).map(ItemView::from);
    }

    @Transactional(readOnly = true)
    public ItemView getPublic(Long id) {
        ItemPost item = require(id);
        if (item.getStatus() == ItemStatus.PENDING_REVIEW || item.getStatus() == ItemStatus.REJECTED) {
            throw new BusinessException("信息不存在或尚未通过审核");
        }
        return ItemView.from(item);
    }

    @Transactional(readOnly = true)
    public List<ItemView> mine(User user) {
        return itemRepository.findByPublisherOrderByCreatedAtDesc(user).stream().map(ItemView::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ItemView> byStatus(ItemStatus status) {
        return itemRepository.findByStatusOrderByCreatedAtDesc(status).stream().map(ItemView::from).toList();
    }

    @Transactional(readOnly = true)
    public long count(ItemStatus status) { return itemRepository.countByStatus(status); }

    @Transactional
    public ItemView review(Long id, boolean approved, String comment) {
        ItemPost item = require(id);
        if (item.getStatus() != ItemStatus.PENDING_REVIEW) throw new BusinessException("该信息已完成审核");
        item.review(approved, comment);
        return ItemView.from(item);
    }

    public ItemPost require(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new BusinessException("信息不存在"));
    }

    public void requireOwner(User user, ItemPost item) {
        if (!item.getPublisher().getId().equals(user.getId())) throw new BusinessException("只能操作自己发布的信息");
    }
}
