package com.sparta.ecommerce.product.controller;

import com.sparta.ecommerce._global.Message;
import com.sparta.ecommerce._global.dto.GlobalDto;
import com.sparta.ecommerce.product.dto.ProductDto;
import com.sparta.ecommerce.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product", description = "상품 API")
@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 목록 조회", description = "전체 상품을 페이지네이션으로 조회합니다")
    @GetMapping("")
    public ResponseEntity<Message> getAllProducts(
            @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size) {
        GlobalDto.PageResponse<ProductDto.Info> response = productService.getProducts(page, size);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 등록", description = "새로운 상품을 등록합니다")
    @PostMapping("")
    public ResponseEntity<Message> registerProduct(@RequestBody ProductDto.Request requestBody) {
        ProductDto.DetailInfo response = productService.registerProduct(requestBody);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 상세 조회", description = "상품 ID로 상품 상세 정보를 조회합니다")
    @GetMapping("/{productId}")
    public ResponseEntity<Message> getProductDetail(
            @Parameter(description = "상품 ID") @PathVariable Long productId) {
        ProductDto.DetailInfo response = productService.getProductDetail(productId);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 수정", description = "상품 정보를 수정합니다. 판매자 인증이 필요합니다")
    @PutMapping("/{productId}")
    public ResponseEntity<Message> updateProduct(
            @Parameter(description = "상품 ID") @PathVariable Long productId,
            @RequestBody ProductDto.Request requestBody) {
        ProductDto.DetailInfo response = productService.updateProduct(productId, requestBody);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다. 판매자 인증이 필요합니다")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Message> deleteProduct(
            @Parameter(description = "상품 ID") @PathVariable Long productId,
            @RequestBody ProductDto.SellerAuth requestBody) {
        productService.deleteProduct(productId, requestBody);
        return new ResponseEntity<>(Message.success(null), HttpStatus.OK);
    }

    @Operation(summary = "상품 접근 권한 확인", description = "상품 수정/삭제를 위한 판매자 권한을 확인합니다")
    @GetMapping("/{productId}/authorization")
    public ResponseEntity<Message> checkAuthorization(
            @Parameter(description = "상품 ID") @PathVariable Long productId,
            @RequestBody ProductDto.SellerAuth requestBody) {
        boolean response = productService.checkAuthorization(productId, requestBody);
        // 해당 위치에서 토큰을 발급하는 등의 보안처리가 필요할 것으로 보임.
        // 발급된 토큰을 기반으로 추후 수정, 삭제 가능 하도록 하는 것이 효용성 있음
        // 현재는 일단 삭제 수정시 비밀번호도 함께 받도록 하였지만 추후 위 방법으로 수정 필요
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }


    @Operation(summary = "유저의 상품 목록 조회", description = "특정 판매자의 상품을 페이지네이션으로 조회합니다")
    @GetMapping("/sellers")
    public ResponseEntity<Message> getSellerProducts(
            @Parameter(description = "판매자 이름") @RequestParam String sellerName,
            @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size) {
        GlobalDto.PageResponse<ProductDto.Info>
                response = productService.getSellerProducts(sellerName, page, size);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

    @Operation(summary = "상품 검색", description = "키워드로 상품을 검색합니다")
    @GetMapping("/search")
    public ResponseEntity<Message> searchProducts(
            @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "검색 키워드") @RequestParam String keyword) {
        GlobalDto.PageResponse<ProductDto.Info>
                response = productService.searchProducts(page, size, keyword);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

}
