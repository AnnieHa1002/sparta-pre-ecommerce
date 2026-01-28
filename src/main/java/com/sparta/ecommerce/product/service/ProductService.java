package com.sparta.ecommerce.product.service;

import com.sparta.ecommerce.product.dto.ProductDto;
import com.sparta.ecommerce.product.entity.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface ProductService {

    @Transactional(readOnly = true)
    Page<ProductDto.Info> getProducts(int page, int size);

    @Transactional
    ProductDto.DetailInfo registerProduct(ProductDto.Request requestBody);

    @Transactional(readOnly = true)
    ProductDto.DetailInfo getProductDetail(Long productId);

    @Transactional
    ProductDto.DetailInfo updateProduct(Long productId, ProductDto.Request requestBody);

    @Transactional
    void deleteProduct(Long productId, ProductDto.SellerAuth requestBody);

    @Transactional(readOnly = true)
    Page<ProductDto.Info> searchProducts(int page, int size, String keyword);

    boolean checkAuthorization(Long productId, ProductDto.SellerAuth requestBody);

    @Transactional(readOnly = true)
    Product getProductEntityById(Long productId);

    @Transactional(readOnly = true)
    Page<ProductDto.Info> getSellerProducts(String sellerName, int page, int size);

    @Transactional
    void decreaseProductCount(Long productId, @NotNull @Min(0) @Max(1) Integer count);
}
