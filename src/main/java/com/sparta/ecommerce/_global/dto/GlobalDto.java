package com.sparta.ecommerce._global.dto;

import com.sparta.ecommerce.order.dto.OrderDto;
import org.springframework.data.domain.Page;

import java.util.List;

public class GlobalDto {
    public record PageResponse<T>(List<T> content, int page, int size, long totalElements,
                                  int totalPages, boolean hasNext) {
        public static <T> PageResponse<T> from(Page<T> page) {
            return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(),
                    page.getTotalElements(), page.getTotalPages(), page.hasNext());
        }

        public static PageResponse<OrderDto.DetailedInfo> empty() {
            return new PageResponse<>(List.of(), 0, 0, 0L, 0, false);
        }
    }

}
