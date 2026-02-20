package com.zest.product_api.service;

import org.springframework.data.domain.*;

import com.zest.product_api.dto.ProductRequest;
import com.zest.product_api.dto.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request, String username);
    ProductResponse getProductById(Long id);
    Page<ProductResponse> getAllProducts(Pageable pageable);
    ProductResponse updateProduct(Long id, ProductRequest request, String username);
    void deleteProduct(Long id);
}
