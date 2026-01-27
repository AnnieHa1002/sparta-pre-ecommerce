package com.sparta.ecommerce.product.controller;

import com.sparta.ecommerce._global.Message;
import com.sparta.ecommerce.product.dto.ProductDto;
import com.sparta.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("")
    public ResponseEntity<Message> getAllProducts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProductDto.Info> response = productService.getProducts(page, size);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Message> registerProduct(@RequestBody ProductDto.Request requestBody) {
        ProductDto.DetailInfo response = productService.registerProduct(requestBody);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Message> getProductDetail(@PathVariable Long productId) {
        ProductDto.DetailInfo response = productService.getProductDetail(productId);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Message> updateProduct(@PathVariable Long productId,
            @RequestBody ProductDto.Request requestBody) {
        ProductDto.DetailInfo response = productService.updateProduct(productId, requestBody);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Message> deleteProduct(@PathVariable Long productId,
            @RequestBody ProductDto.SellerAuth requestBody) {
        productService.deleteProduct(productId, requestBody);
        return new ResponseEntity<>(Message.success(null), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Message> searchProducts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam String keyword) {
        Page<ProductDto.Info> response = productService.searchProducts(page, size, keyword);
        return new ResponseEntity<>(Message.success(response), HttpStatus.OK);
    }

}
