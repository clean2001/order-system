package org.beyond.ordersystem.product.service;

import lombok.RequiredArgsConstructor;
import org.beyond.ordersystem.product.domain.Product;
import org.beyond.ordersystem.product.dto.CreateProductRequest;
import org.beyond.ordersystem.product.dto.ProductResponse;
import org.beyond.ordersystem.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


@RequiredArgsConstructor
@Transactional
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public Long createProduct(CreateProductRequest createProductRequest) {
        MultipartFile image = createProductRequest.getProductImage();
        try {

            Product product = CreateProductRequest.toEntity(createProductRequest);
            Product savedProduct = productRepository.save(product);


            byte[] bytes = image.getBytes();
            Path path = Paths.get("/Users/sejeong/Documents/temp", product.getId() + "_" + image.getOriginalFilename());
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            product.updateImagePath(path.toString()); // dirty checking
            return savedProduct.getId();
        } catch(IOException e) {
            throw new RuntimeException("이미지 저장 실패");
        }
    }

    public Page<ProductResponse> productList(Pageable pageable) {
        Page<Product> productList = productRepository.findAll(pageable);

        return productList
                .map(ProductResponse::fromEntity);
    }
}
