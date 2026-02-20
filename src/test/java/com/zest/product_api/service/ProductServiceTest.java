package com.zest.product_api.service;

import com.zest.product_api.dto.ProductRequest;
import com.zest.product_api.dto.ProductResponse;
import com.zest.product_api.entity.Product;
import com.zest.product_api.exception.ResourceNotFoundException;
import com.zest.product_api.mapper.ProductMapper;
import com.zest.product_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductResponse productResponse;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setProductName("Test Product");
        product.setCreatedBy("admin");
        product.setCreatedOn(LocalDateTime.now());

        productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setProductName("Test Product");
        productResponse.setCreatedBy("admin");

        productRequest = new ProductRequest();
        productRequest.setProductName("Test Product");
    }

    @Test
    void createProduct_ShouldReturnProductResponse() {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.createProduct(productRequest, "admin");

        assertNotNull(result);
        assertEquals("Test Product", result.getProductName());
        assertEquals("admin", result.getCreatedBy());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProductById_WhenExists_ShouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getProductById_WhenNotFound_ShouldThrowException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> productService.getProductById(99L));
    }

    @Test
    void getAllProducts_ShouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product), pageable, 1);

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        Page<ProductResponse> result = productService.getAllProducts(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void updateProduct_WhenExists_ShouldReturnUpdated() {
        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setProductName("Updated Product");

        ProductResponse updatedResponse = new ProductResponse();
        updatedResponse.setId(1L);
        updatedResponse.setProductName("Updated Product");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(updatedResponse);

        ProductResponse result = productService.updateProduct(1L, updateRequest, "admin");

        assertNotNull(result);
        assertEquals("Updated Product", result.getProductName());
    }

    @Test
    void deleteProduct_WhenExists_ShouldDelete() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_WhenNotFound_ShouldThrowException() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
            () -> productService.deleteProduct(99L));
    }
}