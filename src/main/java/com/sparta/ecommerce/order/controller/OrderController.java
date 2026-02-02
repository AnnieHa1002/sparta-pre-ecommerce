package com.sparta.ecommerce.order.controller;

import com.sparta.ecommerce._global.Message;
import com.sparta.ecommerce._global.dto.GlobalDto;
import com.sparta.ecommerce.order.dto.OrderDto;
import com.sparta.ecommerce.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order", description = "주문 API")
@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "상품 주문", description = "상품을 주문합니다")
    @PostMapping("/products/{productId}/orders")
    public ResponseEntity<Message> orderProduct(
            @Parameter(description = "상품 ID") @PathVariable Long productId,
            @Parameter(description = "상품 주문 정보") @RequestBody OrderDto.Request requestBody) {
        OrderDto.DetailedInfo response = orderService.orderProduct(productId, requestBody);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

    @Operation(summary = "유저의 주문 내역 리스트 확인", description = "구매자 이메일로 주문 내역을 조회합니다")
    @GetMapping("/orders")
    public ResponseEntity<Message> getOrdersByBuyerEmail(
            @Parameter(description = "구매자 이메일") @RequestParam String buyerEmail,
            @Parameter(description = "페이지 당 개수") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "커서이름") @RequestParam(required = false) String cursorName) {
        GlobalDto.CursorResponse<OrderDto.Info> response =
                orderService.getOrdersByBuyerEmail(buyerEmail, cursorName, size);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 주문 내역 리스트 확인", description = "판매자 권한으로 상품 ID로 해당 상품의 주문 내역을 조회합니다")
    @GetMapping("/products/{productId}/orders")
    public ResponseEntity<Message> getOrdersByProductId(
            @Parameter(description = "상품 ID") @PathVariable Long productId,
            @Parameter(description = "판매자 비밀번호") @RequestParam String password,
            @Parameter(description = "페이지 당 개수") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "커서이름") @RequestParam(required = false) String cursorName) {
        GlobalDto.CursorResponse<OrderDto.DetailedInfo> response =
                orderService.getOrdersByProductId(productId, password, cursorName, size);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 주문 단건 확인", description = "상품 ID로 해당 상품의 주문 내역을 조회합니다")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Message> getOrderByOrderId(
            @Parameter(description = "주문 ID") @PathVariable Long orderId,
            @Parameter(description = "구매자 비밀번호") @RequestParam String password) {
        OrderDto.DetailedInfo response = orderService.getOrderByOrderId(orderId, password);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

}
