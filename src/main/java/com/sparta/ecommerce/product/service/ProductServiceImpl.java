package com.sparta.ecommerce.product.service;

import com.sparta.ecommerce._global.exception.BusinessException;
import com.sparta.ecommerce._global.utility.EncoderUtils;
import com.sparta.ecommerce.product.dto.ProductDto;
import com.sparta.ecommerce.product.entity.Product;
import com.sparta.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Override
    public Page<ProductDto.Info> getProducts(int page, int size) {
        return null;
    }

    @Override
    public ProductDto.DetailInfo registerProduct(ProductDto.Request requestBody) {
        return null;
    }

    @Override
    public ProductDto.DetailInfo getProductDetail(Long productId) {
        return null;

    }

    @Override
    public ProductDto.DetailInfo updateProduct(Long productId, ProductDto.Request requestBody) {
        return null;
    }

    @Override
    public void deleteProduct(Long productId, ProductDto.SellerAuth requestBody) {

    }

    @Override
    public Page<ProductDto.Info> searchProducts(int page, int size, String keyword) {
        return null;
    }
}
