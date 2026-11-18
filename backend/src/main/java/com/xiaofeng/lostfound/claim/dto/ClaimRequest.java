package com.xiaofeng.lostfound.claim.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record ClaimRequest(@NotBlank(message = "请填写认领证明") @Size(max = 1000, message = "认领证明不能超过1000个字符") String proof) {}
