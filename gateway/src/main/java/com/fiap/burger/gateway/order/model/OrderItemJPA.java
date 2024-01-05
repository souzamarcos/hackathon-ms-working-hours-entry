package com.fiap.burger.gateway.order.model;

import com.fiap.burger.entity.order.OrderItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "order_item")
public class OrderItemJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JoinColumn(name = "order_id", insertable = true, updatable = false)
    @ManyToOne(optional = false)
    OrderJPA order;

    @Column(name = "order_id", insertable = false, updatable = false)
    Long orderId;

    @Column(name = "product_id")
    Long productId;

    @Column
    String comment;

    @OneToMany(mappedBy = "orderItem", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<OrderItemAdditionalJPA> orderItemAdditional;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemJPA that = (OrderItemJPA) o;
        return Objects.equals(id, that.id) && Objects.equals(order, that.order) && Objects.equals(orderId, that.orderId) && Objects.equals(productId, that.productId) && Objects.equals(comment, that.comment) && Objects.equals(orderItemAdditional, that.orderItemAdditional);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, orderId, productId, comment, orderItemAdditional);
    }

    public OrderItemJPA() {

    }

    public OrderItemJPA(
        Long productId,
        String comment,
        OrderJPA orderJPA
    ) {
        this.productId = productId;
        this.comment = comment;
        this.order = orderJPA;
    }

    public OrderItem toEntity() {
        return new OrderItem(
            id,
            orderId,
            productId,
            null,
            comment
        );
    }

    public OrderItem toEntityWithAdditional() {
        return new OrderItem(
            id,
            orderId,
            Optional.ofNullable(orderItemAdditional).map(items -> items.stream().map(OrderItemAdditionalJPA::toEntity).toList()).orElse(null),
            comment,
            productId
        );
    }

    public static OrderItemJPA toJPA(OrderItem orderItem, OrderJPA orderJPA) {
        OrderItemJPA newOrderItem = new OrderItemJPA(
            orderItem.getProductId(),
            orderItem.getComment(),
            orderJPA
        );

        if (!Optional.ofNullable(orderItem.getAdditionalIds()).orElse(Collections.emptyList()).isEmpty()) {
            List<OrderItemAdditionalJPA> itemAdditionals = orderItem.getAdditionalIds().stream().map(itemAdditional -> OrderItemAdditionalJPA.toJPA(itemAdditional, newOrderItem)).toList();
            newOrderItem.setOrderItemAdditional(itemAdditionals);
        }
        return newOrderItem;
    }

    public void setOrderItemAdditional(List<OrderItemAdditionalJPA> orderItemAdditional) {
        this.orderItemAdditional = orderItemAdditional;
    }
}

