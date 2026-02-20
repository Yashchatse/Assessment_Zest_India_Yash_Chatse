package com.zest.product_api.dto;

import jakarta.validation.constraints.*;

public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 255)
    private String productName;

    public ProductRequest() {}

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
}