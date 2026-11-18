package com.xiaofeng.lostfound.admin;
import com.xiaofeng.lostfound.item.*;
import com.xiaofeng.lostfound.item.dto.ItemView;
import com.xiaofeng.lostfound.user.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AdminService {
    private final ItemService items; private final UserRepository users; private final OperationLogRepository logs;
    public AdminService(ItemService items, UserRepository users, OperationLogRepository logs) { this.items = items; this.users = users; this.logs = logs; }
    @Transactional public ItemView review(User admin, Long id, boolean approved, String comment) {
        ItemView view = items.review(id, approved, comment);
        logs.save(new OperationLog(admin, approved ? "审核通过" : "审核驳回", "物品信息", id, comment == null ? "" : comment));
        return view;
    }
    @Transactional(readOnly = true) public Map<String, Long> statistics() {
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("用户总数", users.count()); result.put("待审核", items.count(ItemStatus.PENDING_REVIEW));
        result.put("寻找中", items.count(ItemStatus.SEARCHING)); result.put("认领中", items.count(ItemStatus.CLAIMING));
        result.put("已找回", items.count(ItemStatus.FOUND)); return result;
    }
    @Transactional(readOnly = true) public List<LogView> logs() { return logs.findTop100ByOrderByCreatedAtDesc().stream().map(LogView::from).toList(); }
    public record LogView(Long id, String operator, String action, String targetType, Long targetId, String detail, LocalDateTime createdAt) {
        static LogView from(OperationLog log) { return new LogView(log.getId(), log.getOperator().getNickname(), log.getAction(), log.getTargetType(), log.getTargetId(), log.getDetail(), log.getCreatedAt()); }
    }
}
