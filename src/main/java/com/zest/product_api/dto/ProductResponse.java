package com.zest.product_api.dto;

import java.time.LocalDateTime;

public class ProductResponse {

    private Long id;
    private String productName;
    private String createdBy;
    private LocalDateTime createdOn;
    private String modifiedBy;
    private LocalDateTime modifiedOn;

    public ProductResponse() {}

    public ProductResponse(Long id, String productName, String createdBy,
                           LocalDateTime createdOn, String modifiedBy,
                           LocalDateTime modifiedOn) {
        this.id = id;
        this.productName = productName;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.modifiedBy = modifiedBy;
        this.modifiedOn = modifiedOn;
    }

    public Long getId() { return id; }
    public String getProductName() { return productName; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedOn() { return createdOn; }
    public String getModifiedBy() { return modifiedBy; }
    public LocalDateTime getModifiedOn() { return modifiedOn; }

    public void setId(Long id) { this.id = id; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setCreatedOn(LocalDateTime createdOn) { this.createdOn = createdOn; }
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }
    public void setModifiedOn(LocalDateTime modifiedOn) { this.modifiedOn = modifiedOn; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String productName;
        private String createdBy;
        private LocalDateTime createdOn;
        private String modifiedBy;
        private LocalDateTime modifiedOn;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder productName(String n) { this.productName = n; return this; }
        public Builder createdBy(String c) { this.createdBy = c; return this; }
        public Builder createdOn(LocalDateTime c) { this.createdOn = c; return this; }
        public Builder modifiedBy(String m) { this.modifiedBy = m; return this; }
        public Builder modifiedOn(LocalDateTime m) { this.modifiedOn = m; return this; }

        public ProductResponse build() {
            return new ProductResponse(id, productName, createdBy, createdOn, modifiedBy, modifiedOn);
        }
    }
}