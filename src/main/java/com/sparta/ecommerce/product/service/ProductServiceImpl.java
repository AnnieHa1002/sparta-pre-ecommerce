package com.sparta.ecommerce.product.service;

import com.sparta.ecommerce._global.dto.GlobalDto;
import com.sparta.ecommerce._global.exception.BusinessException;
import com.sparta.ecommerce._global.exception.ExceptionCode;
import com.sparta.ecommerce._global.utility.EncoderUtils;
import com.sparta.ecommerce._global.utility.Utility;
import com.sparta.ecommerce.product.dto.ProductDto;
import com.sparta.ecommerce.product.entity.Product;
import com.sparta.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sparta.ecommerce._global.utility.Utility.getCursorInfo;
import static org.springframework.data.domain.Sort.Direction.DESC;


@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final EncoderUtils encoderUtil;


    @Override
    public GlobalDto.PageResponse<ProductDto.Info> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(DESC, "createdAt"));
        Page<Product> products = productRepository.findAllByIsDeletedIsFalse(pageable);
        Page<ProductDto.Info> productPage = products.map(ProductDto.Info::new);
        return GlobalDto.PageResponse.from(productPage);
    }

    @Override
    public GlobalDto.CursorResponse<ProductDto.Info> getProductsByCursor(String cursor, int size) {
        // 페이지 크기 + 1개 조회 (다음 페이지 존재 여부 확인용)
        Pageable pageable = PageRequest.of(0, size + 1);
        List<Product> products;
        // cursor가 있으면 cursor 이후 데이터 조회
        if (cursor != null) {
            Utility.CursorInfo cursorInfo = getCursorInfo(cursor);
            products = productRepository.findAllWithCursor(cursorInfo.cursorCreatedAt(),
                    cursorInfo.cursorId(), pageable);
        }
        // cursor가 없으면 첫 페이지 조회
        else products = productRepository.findFirstPage(pageable);
        boolean hasNext = products.size() > size;
        if (hasNext) {
            products = products.subList(0, size);  // 초과분 제거
        }

        String nextCursor = null;
        if (hasNext && !products.isEmpty()) {
            Product last = products.getLast();
            nextCursor = Utility.getNextCursor(last);
        }
        return GlobalDto.CursorResponse.fromEntityList(products, hasNext, nextCursor);
    }


    @Override
    public GlobalDto.CursorResponse<ProductDto.Info> getSellerProducts(String cursor,
            String sellerName, int size) {
        // 페이지 크기 + 1개 조회 (다음 페이지 존재 여부 확인용)
        Pageable pageable = PageRequest.of(0, size + 1);
        List<Product> products;
        // cursor가 있으면 cursor 이후 데이터 조회
        if (cursor != null) {
            Utility.CursorInfo cursorInfo = getCursorInfo(cursor);
            products = productRepository.findAllBySellerNameWithCursor(sellerName,
                    cursorInfo.cursorCreatedAt(), cursorInfo.cursorId(), pageable);
        }
        // cursor가 없으면 첫 페이지 조회
        else products = productRepository.findBySellerNameForFirstPage(sellerName, pageable);
        boolean hasNext = products.size() > size;
        if (hasNext) {
            products = products.subList(0, size);  // 초과분 제거
        }

        String nextCursor = null;
        if (hasNext && !products.isEmpty()) {
            Product last = products.getLast();
            nextCursor = Utility.getNextCursor(last);
        }
        return GlobalDto.CursorResponse.fromEntityList(products, hasNext, nextCursor);
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
        boolean sold =
                productRepository.existsByIdAndOrdersIsNotEmptyAndIsDeletedIsFalse(productId);
        if (sold && requestBody.getPrice() != null) {
            throw new BusinessException(ExceptionCode.PRICE_NOT_CHANGEABLE);
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
        checkSellerPassword(requestBody.getSellerPassword(), product.getEncryptedPassword());
        product.update(requestBody);
        return new ProductDto.DetailInfo(product);
    }

    @Override
    public void deleteProduct(Long productId, ProductDto.SellerAuth requestBody) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
        checkSellerPassword(requestBody.getSellerPassword(), product.getEncryptedPassword());
        product.delete();
    }

    @Override
    public GlobalDto.CursorResponse<ProductDto.Info> searchProducts(int page, int size,
            String keyword) {
        throw new BusinessException(ExceptionCode.NOT_IMPLEMENTED);
    }

    @Override
    public boolean checkAuthorization(Long productId, ProductDto.SellerAuth requestBody) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
        return encoderUtil.matches(requestBody.getSellerPassword(), product.getEncryptedPassword());
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
