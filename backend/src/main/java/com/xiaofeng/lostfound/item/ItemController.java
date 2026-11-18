package com.xiaofeng.lostfound.item;

import com.xiaofeng.lostfound.auth.AuthService;
import com.xiaofeng.lostfound.common.ApiResponse;
import com.xiaofeng.lostfound.common.PageView;
import com.xiaofeng.lostfound.item.dto.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService; private final AuthService authService;
    public ItemController(ItemService itemService, AuthService authService) { this.itemService = itemService; this.authService = authService; }
    @GetMapping("/public")
    public ApiResponse<PageView<ItemView>> list(@RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) PostType type,
                                            @RequestParam(required = false) ItemCategory category,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "12") int size) {
        return ApiResponse.ok(PageView.from(itemService.publicList(keyword, type, category, page, size)));
    }
    @GetMapping("/public/{id}") public ApiResponse<ItemView> detail(@PathVariable Long id) { return ApiResponse.ok(itemService.getPublic(id)); }
    @PostMapping public ApiResponse<ItemView> create(@Valid @RequestBody ItemRequest request) { return ApiResponse.ok("发布成功，请等待管理员审核", itemService.create(authService.currentUser(), request)); }
    @PutMapping("/{id}") public ApiResponse<ItemView> update(@PathVariable Long id, @Valid @RequestBody ItemRequest request) { return ApiResponse.ok("修改成功，请等待重新审核", itemService.update(authService.currentUser(), id, request)); }
    @GetMapping("/mine") public ApiResponse<List<ItemView>> mine() { return ApiResponse.ok(itemService.mine(authService.currentUser())); }
}
