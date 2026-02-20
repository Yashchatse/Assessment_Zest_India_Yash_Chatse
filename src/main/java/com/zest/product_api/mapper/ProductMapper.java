package com.zest.product_api.mapper;

import org.springframework.stereotype.Component;

import com.zest.product_api.dto.ProductResponse;
import com.zest.product_api.entity.Product;

@Component
public class ProductMapper {
    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
            .id(product.getId())
            .productName(product.getProductName())
            .createdBy(product.getCreatedBy())
            .createdOn(product.getCreatedOn())
            .modifiedBy(product.getModifiedBy())
            .modifiedOn(product.getModifiedOn())
            .build();
    }
}
