package com.zest.product_api.service;

import java.time.LocalDateTime;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zest.product_api.dto.ProductRequest;
import com.zest.product_api.dto.ProductResponse;
import com.zest.product_api.entity.Product;
import com.zest.product_api.exception.ResourceNotFoundException;
import com.zest.product_api.mapper.ProductMapper;
import com.zest.product_api.repository.ProductRepository;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    public ProductServiceImpl(ProductRepository productRepository,ProductMapper productMapper ) {
    	this.productRepository = productRepository;
    	this.productMapper= productMapper;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request, String username) {
        Product product = Product.builder()
            .productName(request.getProductName())
            .createdBy(username)
            .createdOn(LocalDateTime.now())
            .build();
        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toResponse);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request, String username) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setProductName(request.getProductName());
        product.setModifiedBy(username);
        product.setModifiedOn(LocalDateTime.now());
        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id))
            throw new ResourceNotFoundException("Product not found with id: " + id);
        productRepository.deleteById(id);
    }
}
