package com.xiaofeng.lostfound.notification;
import com.xiaofeng.lostfound.auth.AuthService;
import com.xiaofeng.lostfound.common.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService service; private final AuthService auth;
    public NotificationController(NotificationService service, AuthService auth) { this.service = service; this.auth = auth; }
    @GetMapping public ApiResponse<List<NotificationService.NotificationView>> list() { return ApiResponse.ok(service.list(auth.currentUser())); }
    @PostMapping("/{id}/read") public ApiResponse<Void> read(@PathVariable Long id) { service.read(auth.currentUser(), id); return ApiResponse.ok("已标记为已读", null); }
}
