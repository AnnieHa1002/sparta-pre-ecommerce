package com.sparta.ecommerce.order.dto;

import com.sparta.ecommerce._global.enums.OrderStatus;
import com.sparta.ecommerce.order.entity.Order;
import com.sparta.ecommerce.product.dto.ProductDto;
import com.sparta.ecommerce.product.entity.Product;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class OrderDto {

    /**
     * @param count         0 이상 1 이하
     * @param buyerEmail    이메일 형식
     * @param buyerAddress  비어있지 않음
     * @param buyerPostcode 비어있지 않음 + 우편번호 형식
     */
    public record Request(@NotNull @Min(0) @Max(1) Integer count,
                          @Email @NotBlank String buyerEmail, @NotBlank String buyerAddress,
                          @NotBlank @Pattern(regexp = "^[0-9]{5}$") String buyerPostcode,
                          @NotBlank String password) {}


    @Getter
    @AllArgsConstructor
    public static class Info {
        private final Long orderId;
        private final Integer count;
        private final ProductDto.Info productInfo;

        public Info(Order order, Product product) {
            this.orderId = order.getId();
            this.count = order.getCount();
            this.productInfo = new ProductDto.Info(product);
        }
    }

    @Getter
    public static class DetailedInfo extends Info {
        private final BuyerInfo buyerInfo;
        private final OrderStatus orderStatus;

        public DetailedInfo(Order order, Product product) {
            super(order, product);
            this.orderStatus = order.getStatus();
            this.buyerInfo = new BuyerInfo(order);
        }
    }

    @Getter
    public static class BuyerInfo {
        private final String buyerEmail;
        private final String buyerAddress;
        private final String buyerPostcode;

        public BuyerInfo(com.sparta.ecommerce.order.entity.Order order) {
            this.buyerEmail = order.getBuyerEmail();
            this.buyerAddress = order.getBuyerAddress();
            this.buyerPostcode = order.getBuyerPostcode();
        }
    }
}
