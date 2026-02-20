package com.zest.product_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    // ── Constructors ──────────────────────────────────────
    public Item() {}

    public Item(Long id, Product product, Integer quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    // ── Getters ───────────────────────────────────────────
    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public Integer getQuantity() { return quantity; }

    // ── Setters ───────────────────────────────────────────
    public void setId(Long id) { this.id = id; }
    public void setProduct(Product product) { this.product = product; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    // ── Builder ───────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Product product;
        private Integer quantity;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder product(Product product) { this.product = product; return this; }
        public Builder quantity(Integer quantity) { this.quantity = quantity; return this; }

        public Item build() {
            return new Item(id, product, quantity);
        }
    }
}