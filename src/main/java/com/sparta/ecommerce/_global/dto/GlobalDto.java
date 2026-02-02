package com.sparta.ecommerce._global.dto;

import com.sparta.ecommerce.order.dto.OrderDto;
import com.sparta.ecommerce.product.dto.ProductDto;
import com.sparta.ecommerce.product.entity.Product;
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

    public record CursorResponse<T>(List<T> content, int size, boolean hasNext, String nextCursor) {

        public static CursorResponse<OrderDto.DetailedInfo> empty() {
            return new CursorResponse<>(List.of(), 0, false, null);
        }

        public static CursorResponse<ProductDto.Info> fromEntityList(List<Product> products,
                boolean hasNext, String nextCursor) {
            List<ProductDto.Info> productInfos = products.stream()
                    .map(ProductDto.Info::new)
                    .toList();
            return new CursorResponse<>(productInfos, productInfos.size(), hasNext, nextCursor);
        }
    }

}
