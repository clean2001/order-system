package org.beyond.ordersystem.product.controller;

import lombok.RequiredArgsConstructor;
import org.beyond.ordersystem.common.dto.SuccessResponse;
import org.beyond.ordersystem.product.dto.CreateProductRequest;
import org.beyond.ordersystem.product.dto.ProductResponse;
import org.beyond.ordersystem.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;

    @GetMapping("/product/create")
    public ResponseEntity<SuccessResponse> createProduct(CreateProductRequest createProductRequest) {
        Long productId = productService.createProduct(createProductRequest);

        SuccessResponse response = SuccessResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .result(productId)
                .statusMessage("상품이 등록되었습니다.")
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/product/list")
    public ResponseEntity<SuccessResponse> productList(@PageableDefault(size = 10) Pageable pageable) {
        Page<ProductResponse> productList = productService.productList(pageable);
        SuccessResponse response = SuccessResponse.builder()
                .statusMessage("상품 리스트입니다.")
                .httpStatus(HttpStatus.OK)
                .result(productList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
