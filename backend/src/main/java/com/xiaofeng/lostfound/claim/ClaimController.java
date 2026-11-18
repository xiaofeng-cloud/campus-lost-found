package com.xiaofeng.lostfound.claim;
import com.xiaofeng.lostfound.auth.AuthService;
import com.xiaofeng.lostfound.claim.dto.*;
import com.xiaofeng.lostfound.common.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/claims")
public class ClaimController {
    private final ClaimService service; private final AuthService auth;
    public ClaimController(ClaimService service, AuthService auth) { this.service = service; this.auth = auth; }
    @PostMapping("/item/{itemId}") public ApiResponse<ClaimView> submit(@PathVariable Long itemId, @Valid @RequestBody ClaimRequest request) { return ApiResponse.ok("认领申请已提交", service.submit(auth.currentUser(), itemId, request)); }
    @GetMapping("/mine") public ApiResponse<List<ClaimView>> mine() { return ApiResponse.ok(service.mine(auth.currentUser())); }
    @GetMapping("/received") public ApiResponse<List<ClaimView>> received() { return ApiResponse.ok(service.received(auth.currentUser())); }
    @PostMapping("/{id}/process") public ApiResponse<ClaimView> process(@PathVariable Long id, @RequestBody ProcessRequest request) { return ApiResponse.ok("认领申请已处理", service.process(auth.currentUser(), id, request.approved(), request.comment())); }
    @PostMapping("/{id}/confirm-returned") public ApiResponse<ClaimView> confirm(@PathVariable Long id) { return ApiResponse.ok("已确认归还", service.confirmReturned(auth.currentUser(), id)); }
    public record ProcessRequest(boolean approved, @Size(max = 500, message = "处理意见不能超过500个字符") String comment) {}
}
