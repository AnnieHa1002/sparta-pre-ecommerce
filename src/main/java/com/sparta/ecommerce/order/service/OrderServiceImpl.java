package com.sparta.ecommerce.order.service;

import com.sparta.ecommerce._global.component.CursorCode;
import com.sparta.ecommerce._global.dto.GlobalDto;
import com.sparta.ecommerce._global.exception.BusinessException;
import com.sparta.ecommerce._global.exception.ExceptionCode;
import com.sparta.ecommerce._global.utility.EncoderUtils;
import com.sparta.ecommerce.order.dto.OrderDto;
import com.sparta.ecommerce.order.entity.Order;
import com.sparta.ecommerce.order.repository.OrderRepository;
import com.sparta.ecommerce.product.entity.Product;
import com.sparta.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final EncoderUtils encoderUtils;
    private final CursorCode cursorCode;

    @Override
    public OrderDto.DetailedInfo orderProduct(Long productId, OrderDto.Request requestBody) {
        productService.decreaseProductCount(productId, requestBody.count());
        Product product = productService.getProductEntityById(productId);
        String encryptedPassword = encoderUtils.encrypt(requestBody.password());
        com.sparta.ecommerce.order.entity.Order order =
                new com.sparta.ecommerce.order.entity.Order(product, requestBody,
                        encryptedPassword);
        orderRepository.save(order);
        return new OrderDto.DetailedInfo(order, product);
    }

    @Override
    public GlobalDto.CursorResponse<OrderDto.Info> getOrdersByBuyerEmail(String buyerEmail,
            String cursor, int size) {
        // 페이지 크기 + 1개 조회 (다음 페이지 존재 여부 확인용)
        Pageable pageable = PageRequest.of(0, size + 1);
        List<Order> orders;
        // cursor가 있으면 cursor 이후 데이터 조회
        if (cursor != null) {
            CursorCode.CursorInfo cursorInfo = cursorCode.decode(cursor);
            orders = orderRepository.findAllByBuyerEmailWithCursor(buyerEmail,
                    cursorInfo.cursorCreatedAt(), cursorInfo.cursorId(), pageable);
        }
        // cursor가 없으면 첫 페이지 조회
        else orders = orderRepository.findByBuyerEmailForFirstPage(buyerEmail, pageable);
        boolean hasNext = orders.size() > size;
        if (hasNext) {
            orders = orders.subList(0, size);  // 초과분 제거
        }

        String nextCursor = null;
        if (hasNext && !orders.isEmpty()) {
            Order last = orders.getLast();
            nextCursor = cursorCode.encode(last.getCreatedAt(), last.getId());
        }
        return GlobalDto.CursorResponse.fromEntityListToInfo(orders, hasNext, nextCursor);
    }

    @Override
    public GlobalDto.CursorResponse<OrderDto.DetailedInfo> getOrdersByProductId(Long productId,
            String password, String cursor, int size) {
        // 페이지 크기 + 1개 조회 (다음 페이지 존재 여부 확인용)
        Pageable pageable = PageRequest.of(0, size + 1);
        Product product = productService.getProductEntityById(productId);
        if (!encoderUtils.matches(password, product.getEncryptedPassword()))
            throw new BusinessException(ExceptionCode.UNAUTHORIZED);
        List<Order> orders;
        // cursor가 있으면 cursor 이후 데이터 조회
        if (cursor != null) {
            CursorCode.CursorInfo cursorInfo = cursorCode.decode(cursor);
            orders = orderRepository.findAllByProductIdWithCursor(productId,
                    cursorInfo.cursorCreatedAt(), cursorInfo.cursorId(), pageable);
        }
        // cursor가 없으면 첫 페이지 조회
        else orders = orderRepository.findByProductIdForFirstPage(productId, pageable);
        boolean hasNext = orders.size() > size;
        if (hasNext) {
            orders = orders.subList(0, size);  // 초과분 제거
        }

        String nextCursor = null;
        if (hasNext && !orders.isEmpty()) {
            Order last = orders.getLast();
            nextCursor = cursorCode.encode(last.getCreatedAt(), last.getId());
        }
        return GlobalDto.CursorResponse.fromEntityListToDetailedInfo(orders, product, hasNext,
                nextCursor);

    }

    @Override
    public OrderDto.DetailedInfo getOrderByOrderId(Long orderId, String password) {
        com.sparta.ecommerce.order.entity.Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.ORDER_NOT_FOUND));
        if (!encoderUtils.matches(password, order.getEncryptedPassword()))
            throw new BusinessException(ExceptionCode.INVALID_ORDER_PASSWORD);
        Product product = order.getProduct();
        return new OrderDto.DetailedInfo(order, product);
    }


}
