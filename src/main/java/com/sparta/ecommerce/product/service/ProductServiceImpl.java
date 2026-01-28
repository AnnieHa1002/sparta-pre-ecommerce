package com.sparta.ecommerce.product.service;

import com.sparta.ecommerce._global.exception.BusinessException;
import com.sparta.ecommerce._global.exception.ExceptionCode;
import com.sparta.ecommerce._global.utility.EncoderUtils;
import com.sparta.ecommerce.product.dto.ProductDto;
import com.sparta.ecommerce.product.entity.Product;
import com.sparta.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.Sort.Direction.DESC;


@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final EncoderUtils encoderUtil;


    @Override
    public Page<ProductDto.Info> getProducts(int page, int size) {
        //id  descending
        Pageable pageable = PageRequest.of(page, size, Sort.by(DESC, "createdAt"));
        Page<Product> products = productRepository.findAllByIsDeletedIsFalse(pageable);
        return products.map(ProductDto.Info::new);
    }

    @Override
    public Page<ProductDto.Info> getSellerProducts(String sellerName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(DESC, "createdAt"));
        Page<Product> products = productRepository.findAllBySellerNameAndIsDeletedIsFalse(sellerName, pageable);
        return products.map(ProductDto.Info::new);
    }

    @Override
    public void decreaseProductCount(Long productId, Integer count) {
        int updatedRows = productRepository.decreaseStock(productId, count);
        if (updatedRows == 0) {
            throw new BusinessException(ExceptionCode.INSUFFICIENT_STOCK);
        }
    }


    @Override
    public ProductDto.DetailInfo registerProduct(ProductDto.Request requestBody) {
        String encodedPassword = encoderUtil.encrypt(requestBody.getSellerPassword());
        Product product = new Product(requestBody, encodedPassword);
        productRepository.save(product);
        return new ProductDto.DetailInfo(product);
    }

    @Override
    public ProductDto.DetailInfo getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
        return new ProductDto.DetailInfo(product);
    }

    @Override
    public ProductDto.DetailInfo updateProduct(Long productId, ProductDto.Request requestBody) {
        boolean sold = productRepository.existsByIdAndOrdersIsNotEmptyAndIsDeletedIsFalse(productId);
        if (sold && requestBody.getPrice() != null) {
            throw new BusinessException(ExceptionCode.PRICE_NOT_CHANGEABLE);
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
        checkSellerPassword(requestBody.getSellerPassword(), product.getPassword());
        product.update(requestBody);
        return new ProductDto.DetailInfo(product);
    }

    @Override
    public void deleteProduct(Long productId, ProductDto.SellerAuth requestBody) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
        checkSellerPassword(requestBody.getSellerPassword(), product.getPassword());
        product.delete();
    }

    @Override
    public Page<ProductDto.Info> searchProducts(int page, int size, String keyword) {
        throw new BusinessException(ExceptionCode.NOT_IMPLEMENTED);
    }

    @Override
    public boolean checkAuthorization(Long productId, ProductDto.SellerAuth requestBody) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
        return encoderUtil.matches(requestBody.getSellerPassword(), product.getPassword());
    }

    @Override
    public Product getProductEntityById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
    }

    protected void checkSellerPassword(String rawPassword, String encryptedPassword) {
        if (!encoderUtil.matches(rawPassword, encryptedPassword))
            throw new BusinessException(ExceptionCode.UNAUTHORIZED);
    }
}
