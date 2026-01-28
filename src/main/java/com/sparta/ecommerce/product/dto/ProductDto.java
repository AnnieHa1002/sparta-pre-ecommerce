package com.sparta.ecommerce.product.dto;

import com.sparta.ecommerce.product.entity.Product;
import lombok.Getter;

public class ProductDto {
    @Getter
    public static class Info {
        private final Long id;
        private final String name;
        private final Integer price;
        private final String currency;
        private final Boolean isHidden;

        public Info(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.currency = product.getCurrency();
            this.isHidden = product.getIsHidden();
        }
    }

    @Getter
    public static class DetailInfo extends Info {
        private final String description;
        private final String imageUrl;
        private final String sellerName;
        private final String sellerEmail;

        public DetailInfo(Product product) {
            super(product);
            this.description = product.getDescription();
            this.imageUrl = product.getImageUrl();
            this.sellerName = product.getSellerName();
            this.sellerEmail = product.getSellerEmail();
        }
    }

    @Getter
    public static class Request extends SellerAuth {
        private String name;
        private Integer price;
        private String currency;
        private Integer stock;
        private String description;
        private String imageUrl;
    }

    @Getter
    public static class SellerAuth {
        private String sellerName;
        private String sellerEmail;
        private String sellerPassword;
    }


}
