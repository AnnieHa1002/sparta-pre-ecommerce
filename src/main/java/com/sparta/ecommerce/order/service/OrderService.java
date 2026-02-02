package com.sparta.ecommerce.order.service;


import com.sparta.ecommerce._global.dto.GlobalDto;
import com.sparta.ecommerce.order.dto.OrderDto;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {
    @Transactional
    OrderDto.DetailedInfo orderProduct(Long productId, OrderDto.Request requestBody);

    @Transactional(readOnly = true)
    GlobalDto.CursorResponse<OrderDto.Info> getOrdersByBuyerEmail(String buyerEmail, String cursor,
            int size);

    @Transactional(readOnly = true)
    GlobalDto.CursorResponse<OrderDto.DetailedInfo> getOrdersByProductId(Long productId,
            String password, String cursor, int size);

    @Transactional(readOnly = true)
    OrderDto.DetailedInfo getOrderByOrderId(Long orderId, String password);
}
