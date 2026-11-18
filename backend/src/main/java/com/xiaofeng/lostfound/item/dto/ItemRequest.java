package com.xiaofeng.lostfound.item.dto;

import com.xiaofeng.lostfound.item.ItemCategory;
import com.xiaofeng.lostfound.item.PostType;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record ItemRequest(
        @NotNull(message = "请选择信息类型") PostType postType,
        @NotBlank(message = "标题不能为空") @Size(max = 120, message = "标题不能超过120个字符") String title,
        @NotBlank(message = "物品描述不能为空") @Size(max = 2000, message = "描述不能超过2000个字符") String description,
        @NotNull(message = "请选择物品分类") ItemCategory category,
        @NotBlank(message = "地点不能为空") @Size(max = 160, message = "地点不能超过160个字符") String location,
        @NotNull(message = "请选择发生时间") @PastOrPresent(message = "发生时间不能晚于当前时间") LocalDateTime occurredAt,
        @NotBlank(message = "联系方式不能为空") @Size(max = 100, message = "联系方式不能超过100个字符") String contact,
        @Size(max = 500, message = "图片地址不能超过500个字符") String imageUrl
) {}
