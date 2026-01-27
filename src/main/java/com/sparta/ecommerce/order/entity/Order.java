package com.sparta.ecommerce.order.entity;

import com.sparta.ecommerce._global.utility.Timestamped;
import com.sparta.ecommerce.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Order extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private String buyerName;

    @Column(nullable = false)
    private String buyerEmail;


    @Column(nullable = false)
    private String buyerAddress;

    @Column(nullable = false)
    private String buyerPostcode;

    private String password;

}
