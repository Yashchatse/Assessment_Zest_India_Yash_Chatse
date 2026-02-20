package com.zest.product_api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zest.product_api.dto.ApiResponse;
import com.zest.product_api.dto.ItemResponse;
import com.zest.product_api.dto.ProductRequest;
import com.zest.product_api.dto.ProductResponse;
import com.zest.product_api.service.ProductService;
import com.zest.product_api.service.ItemService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/products")

@Tag(name = "Products", description = "Product management APIs")
public class ProductController {

    private final ProductService productService;
    private final ItemService itemService;
    
    public ProductController(ProductService productService,ItemService itemService){
    	 this.productService = productService;
    	 this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(
            productService.getAllProducts(pageable), "Products fetched"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
            productService.getProductById(id), "Product fetched"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(
            @Valid @RequestBody ProductRequest request,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
            productService.createProduct(request, auth.getName()), "Product created"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request,
            Authentication auth) {
        return ResponseEntity.ok(ApiResponse.success(
            productService.updateProduct(id, request, auth.getName()), "Product updated"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted"));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<ApiResponse<Page<ItemResponse>>> getItems(
            @PathVariable Long id,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(
            itemService.getItemsByProduct(id, pageable), "Items fetched"));
    }
}
