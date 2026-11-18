package com.xiaofeng.lostfound.admin;
import com.xiaofeng.lostfound.auth.AuthService;
import com.xiaofeng.lostfound.common.ApiResponse;
import com.xiaofeng.lostfound.item.*;
import com.xiaofeng.lostfound.item.dto.ItemView;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/admin")
public class AdminController {
    private final AdminService admin; private final ItemService items; private final AuthService auth;
    public AdminController(AdminService admin, ItemService items, AuthService auth) { this.admin = admin; this.items = items; this.auth = auth; }
    @GetMapping("/pending-items") public ApiResponse<List<ItemView>> pending() { return ApiResponse.ok(items.byStatus(ItemStatus.PENDING_REVIEW)); }
    @PostMapping("/items/{id}/review") public ApiResponse<ItemView> review(@PathVariable Long id, @RequestBody ReviewRequest request) { return ApiResponse.ok("审核完成", admin.review(auth.currentUser(), id, request.approved(), request.comment())); }
    @GetMapping("/statistics") public ApiResponse<Map<String, Long>> statistics() { return ApiResponse.ok(admin.statistics()); }
    @GetMapping("/logs") public ApiResponse<List<AdminService.LogView>> logs() { return ApiResponse.ok(admin.logs()); }
    public record ReviewRequest(boolean approved, @Size(max = 500, message = "审核意见不能超过500个字符") String comment) {}
}
