package com.fiap.burger.entity.order;

import com.fiap.burger.entity.product.Product;

import java.util.List;
import java.util.Objects;

public class OrderItem {

    Long id;
    Long orderId;
    Long productId;
    List<Long> additionalIds;
    String comment;

    Product product;

    List<OrderItemAdditional> orderItemAdditionals;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem orderItem)) return false;
        return Objects.equals(hashCode(), orderItem.hashCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            getOrderId(),
            getProductId(),
            getComment(),
            getProduct(),
            getOrderItemAdditionals()
        );
    }

    public Long getId() {
        return id;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public List<Long> getAdditionalIds() {
        return additionalIds;
    }

    public String getComment() {
        return comment;
    }

    public Product getProduct() {
        return product;
    }

    public List<OrderItemAdditional> getOrderItemAdditionals() {
        return orderItemAdditionals;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderItem(Long orderId, Long productId, String comment) {
        this.orderId = orderId;
        this.productId = productId;
        this.comment = comment;
    }

    public OrderItem(Long id, Long orderId, Long productId, List<Long> additionalIds, String comment) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.additionalIds = additionalIds;
        this.comment = comment;
    }

    public OrderItem(Long id, Long orderId, List<OrderItemAdditional> orderItemAdditionals, String comment, Long productId) {
        this.id = id;
        this.orderId = orderId;
        this.orderItemAdditionals = orderItemAdditionals;
        this.comment = comment;
        this.productId = productId;
    }

    public OrderItem(Long id, Long orderId, List<OrderItemAdditional> orderItemAdditionals, String comment, Product product) {
        this.id = id;
        this.orderId = orderId;
        this.orderItemAdditionals = orderItemAdditionals;
        this.comment = comment;
        this.product = product;
    }
}
