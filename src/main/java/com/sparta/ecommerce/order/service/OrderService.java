package com.sparta.ecommerce.order.service;


import com.sparta.ecommerce.order.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {
    @Transactional
    OrderDto.DetailedInfo orderProduct(Long productId, OrderDto.Request requestBody);

    @Transactional(readOnly = true)
    Page<OrderDto.Info> getOrdersByBuyerEmail(String buyerEmail, int size, int page);

    @Transactional(readOnly = true)
    Page<OrderDto.DetailedInfo> getOrdersByProductId(Long productId, String password, int size, int page);

    @Transactional(readOnly = true)
    OrderDto.DetailedInfo getOrderByOrderId(Long orderId, String password);
}
