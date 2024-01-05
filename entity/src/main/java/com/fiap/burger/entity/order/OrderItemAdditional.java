package com.fiap.burger.entity.order;

import com.fiap.burger.entity.product.Product;

import java.util.Objects;

public class OrderItemAdditional {
    Long id;

    Long orderItemId;
    Long productId;

    Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItemAdditional orderItemAdditional)) return false;
        return Objects.equals(hashCode(), orderItemAdditional.hashCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            getId(),
            getOrderItemId(),
            getProductId(),
            getProduct()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderItemAdditional(Long id, Long orderItemId, Long productId) {
        this.id = id;
        this.orderItemId = orderItemId;
        this.productId = productId;
    }

    public OrderItemAdditional(Long orderItemId, Long productId) {
        this.orderItemId = orderItemId;
        this.productId = productId;
    }

    public OrderItemAdditional(Long id, Long orderItemId, Product product) {
        this.id = id;
        this.orderItemId = orderItemId;
        this.product = product;
    }
}

