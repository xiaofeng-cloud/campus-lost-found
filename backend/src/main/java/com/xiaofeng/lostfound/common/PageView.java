package com.xiaofeng.lostfound.common;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageView<T>(List<T> content, long totalElements, int totalPages, int number, int size) {
    public static <T> PageView<T> from(Page<T> page) {
        return new PageView<>(page.getContent(), page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize());
    }
}
