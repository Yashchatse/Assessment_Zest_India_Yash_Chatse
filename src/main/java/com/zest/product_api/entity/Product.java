package com.zest.product_api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product", indexes = {
    @Index(name = "idx_product_name", columnList = "product_name")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items;

    // ── Constructors ──────────────────────────────────────
    public Product() {}

    public Product(Long id, String productName, String createdBy,
                   LocalDateTime createdOn, String modifiedBy,
                   LocalDateTime modifiedOn, List<Item> items) {
        this.id = id;
        this.productName = productName;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.modifiedBy = modifiedBy;
        this.modifiedOn = modifiedOn;
        this.items = items;
    }

    // ── Getters ───────────────────────────────────────────
    public Long getId() { return id; }
    public String getProductName() { return productName; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedOn() { return createdOn; }
    public String getModifiedBy() { return modifiedBy; }
    public LocalDateTime getModifiedOn() { return modifiedOn; }
    public List<Item> getItems() { return items; }

    // ── Setters ───────────────────────────────────────────
    public void setId(Long id) { this.id = id; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setCreatedOn(LocalDateTime createdOn) { this.createdOn = createdOn; }
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }
    public void setModifiedOn(LocalDateTime modifiedOn) { this.modifiedOn = modifiedOn; }
    public void setItems(List<Item> items) { this.items = items; }

    // ── Builder ───────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String productName;
        private String createdBy;
        private LocalDateTime createdOn;
        private String modifiedBy;
        private LocalDateTime modifiedOn;
        private List<Item> items;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder productName(String productName) { this.productName = productName; return this; }
        public Builder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public Builder createdOn(LocalDateTime createdOn) { this.createdOn = createdOn; return this; }
        public Builder modifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; return this; }
        public Builder modifiedOn(LocalDateTime modifiedOn) { this.modifiedOn = modifiedOn; return this; }
        public Builder items(List<Item> items) { this.items = items; return this; }

        public Product build() {
            return new Product(id, productName, createdBy, createdOn, modifiedBy, modifiedOn, items);
        }
    }
}