package com.sparta.ecommerce.order.entity;

import com.sparta.ecommerce._global.enums.OrderStatus;
import com.sparta.ecommerce._global.utility.Timestamped;
import com.sparta.ecommerce.order.dto.OrderDto;
import com.sparta.ecommerce.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private String buyerEmail;

    @Column(nullable = false)
    private String buyerAddress;

    @Column(nullable = false)
    private String buyerPostcode;

    @Column(nullable = false)
    private String encryptedPassword;

    @Column(nullable = false)
    @Setter
    private OrderStatus status = OrderStatus.ORDERED;

    public Order(Product product, OrderDto.Request requestBody, String encryptedPassword) {
        this.product = product;
        this.count = requestBody.count();
        this.buyerEmail = requestBody.buyerEmail();
        this.buyerAddress = requestBody.buyerAddress();
        this.buyerPostcode = requestBody.buyerPostcode();
        this.encryptedPassword = encryptedPassword;
    }
}
