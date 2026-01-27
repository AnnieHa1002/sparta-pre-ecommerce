package com.sparta.ecommerce.product.entity;

import com.sparta.ecommerce._global.utility.Timestamped;
import com.sparta.ecommerce.order.entity.Order;
import com.sparta.ecommerce.product.dto.ProductDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Product extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Setter
    @Column(nullable = false, columnDefinition = "VARCHAR(500)")
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String currency;

    @Setter
    @Column(nullable = false)
    private Integer stock;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    @Column(nullable = false)
    private String sellerName;

    @Column(nullable = false)
    private String sellerEmail;

    private String password;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column(nullable = false)
    private Boolean isHidden = false;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;


    public Product(ProductDto.Request requestBody, String encriptedPassword) {
        this.name = requestBody.getName();
        this.price = requestBody.getPrice();
        this.currency = requestBody.getCurrency();
        this.stock = requestBody.getStock();
        this.description = requestBody.getDescription();
        this.imageUrl = requestBody.getImageUrl();
        this.sellerName = requestBody.getSellerName();
        this.sellerEmail = requestBody.getSellerEmail();
        this.password = encriptedPassword;
    }


    public void update(ProductDto.Request requestBody) {
        if (requestBody.getName() != null) {
            this.name = requestBody.getName();
        }
        if (requestBody.getPrice() != null) {
            this.price = requestBody.getPrice();
        }
        if (requestBody.getStock() != null) {
            this.stock = requestBody.getStock();
        }
        if (requestBody.getDescription() != null) {
            this.description = requestBody.getDescription();
        }
        if (requestBody.getImageUrl() != null) {
            this.imageUrl = requestBody.getImageUrl();
        }
    }
}
