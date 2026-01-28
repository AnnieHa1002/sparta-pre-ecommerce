package com.sparta.ecommerce.order.service;

import com.sparta.ecommerce._global.exception.BusinessException;
import com.sparta.ecommerce._global.exception.ExceptionCode;
import com.sparta.ecommerce._global.utility.EncoderUtils;
import com.sparta.ecommerce.order.dto.OrderDto;
import com.sparta.ecommerce.order.entity.Order;
import com.sparta.ecommerce.order.repository.OrderRepository;
import com.sparta.ecommerce.product.entity.Product;
import com.sparta.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final EncoderUtils encoderUtils;

    @Override
    public OrderDto.DetailedInfo orderProduct(Long productId, OrderDto.Request requestBody) {
        productService.decreaseProductCount(productId, requestBody.count());
        Product product = productService.getProductEntityById(productId);
        String encryptedPassword = encoderUtils.encrypt(requestBody.password());
        Order order = new Order(product, requestBody, encryptedPassword);
        orderRepository.save(order);
        return new OrderDto.DetailedInfo(order, product);
    }

    @Override
    public Page<OrderDto.Info> getOrdersByBuyerEmail(String buyerEmail, int size, int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> orders = orderRepository.findAllByBuyerEmail(buyerEmail, pageable);
        return orders.map(order -> new OrderDto.Info(order, order.getProduct()));
    }

    @Override
    public Page<OrderDto.DetailedInfo> getOrdersByProductId(Long productId, String password,
            int size, int page) {
        Product product = productService.getProductEntityById(productId);
        if (encoderUtils.matches(password, product.getPassword())) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Order> orders = orderRepository.findAllByProductId(productId, pageable);
            return orders.map(order -> new OrderDto.DetailedInfo(order, product));
        }
        return Page.empty();
    }

    @Override
    public OrderDto.DetailedInfo getOrderByOrderId(Long orderId, String password) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.ORDER_NOT_FOUND));
        if (!encoderUtils.matches(password, order.getEncryptedPassword()))
            throw new BusinessException(ExceptionCode.INVALID_ORDER_PASSWORD);
        Product product = order.getProduct();
        return new OrderDto.DetailedInfo(order, product);
    }


}
