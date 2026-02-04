package com.sparta.ecommerce.product.service;

import com.sparta.ecommerce._global.dto.GlobalDto;
import com.sparta.ecommerce.product.dto.ProductDto;
import com.sparta.ecommerce.product.entity.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ProductService {

    @Transactional(readOnly = true)
    GlobalDto.PageResponse<ProductDto.Info> getProducts(int page, int size);

    @Transactional
    ProductDto.DetailInfo registerProduct(ProductDto.Request requestBody);

    @Transactional(readOnly = true)
    ProductDto.DetailInfo getProductDetail(Long productId);

    @Transactional
    ProductDto.DetailInfo updateProduct(Long productId, ProductDto.Request requestBody);

    @Transactional
    void deleteProduct(Long productId, ProductDto.SellerAuth requestBody);

    @Transactional(readOnly = true)
    GlobalDto.CursorResponse<ProductDto.Info> searchProducts(int page, int size, String keyword);

    boolean checkAuthorization(Long productId, ProductDto.SellerAuth requestBody);

    @Transactional(readOnly = true)
    Product getProductEntityById(Long productId);

    GlobalDto.CursorResponse<ProductDto.Info> getProductsByCursor(String cursor, int size);

    @Transactional(readOnly = true)
    GlobalDto.CursorResponse<ProductDto.Info> getSellerProducts(String cursor, String sellerName,
            int size);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    boolean decreaseProductCount(Long productId, @NotNull @Min(0) @Max(1) Integer count);
}
