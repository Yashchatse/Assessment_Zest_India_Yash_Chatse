package com.zest.product_api.dto;

public class ItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;

    public ItemResponse() {}

    public ItemResponse(Long id, Long productId, String productName, Integer quantity) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }

    public void setId(Long id) { this.id = id; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long productId;
        private String productName;
        private Integer quantity;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder productId(Long productId) { this.productId = productId; return this; }
        public Builder productName(String productName) { this.productName = productName; return this; }
        public Builder quantity(Integer quantity) { this.quantity = quantity; return this; }

        public ItemResponse build() {
            return new ItemResponse(id, productId, productName, quantity);
        }
    }
}