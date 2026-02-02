package com.sparta.ecommerce._global.dto;

import com.sparta.ecommerce.order.dto.OrderDto;
import com.sparta.ecommerce.order.entity.Order;
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


        public static CursorResponse<OrderDto.DetailedInfo> fromEntityListToDetailedInfo(
                List<Order> orders, Product product, boolean hasNext, String nextCursor) {
            List<OrderDto.DetailedInfo> orderInfos = orders.stream()
                    .map(order -> new OrderDto.DetailedInfo(order, product))
                    .toList();
            return new CursorResponse<>(orderInfos, orderInfos.size(), hasNext, nextCursor);
        }

        public static CursorResponse<OrderDto.DetailedInfo> fromEntityListToDetailedInfoAndNoProduct(
                List<Order> orders, boolean hasNext, String nextCursor) {
            List<OrderDto.DetailedInfo> orderInfos = orders.stream()
                    .map(order -> new OrderDto.DetailedInfo(order, order.getProduct()))
                    .toList();
            return new CursorResponse<>(orderInfos, orderInfos.size(), hasNext, nextCursor);
        }

        public static CursorResponse<OrderDto.Info> fromEntityListToInfo(List<Order> orders,
                boolean hasNext, String nextCursor) {
            List<OrderDto.Info> orderInfos = orders.stream()
                    .map(order -> new OrderDto.Info(order, order.getProduct()))
                    .toList();
            return new CursorResponse<>(orderInfos, orderInfos.size(), hasNext, nextCursor);
        }
    }

}
