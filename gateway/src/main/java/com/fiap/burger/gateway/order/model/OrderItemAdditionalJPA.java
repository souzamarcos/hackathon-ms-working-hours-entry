package com.fiap.burger.gateway.order.model;

import com.fiap.burger.entity.order.OrderItemAdditional;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "order_item_additional")
public class OrderItemAdditionalJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JoinColumn(name = "order_item_id", insertable = true, updatable = false)
    @ManyToOne(optional = false)
    OrderItemJPA orderItem;
    @Column(name = "order_item_id", insertable = false, updatable = false)
    Long orderItemId;

    @Column(name = "product_id")
    Long productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemAdditionalJPA that = (OrderItemAdditionalJPA) o;
        return Objects.equals(id, that.id) && Objects.equals(orderItem, that.orderItem) && Objects.equals(orderItemId, that.orderItemId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderItem, orderItemId, productId);
    }

    public OrderItemAdditionalJPA(OrderItemJPA orderItem, Long productId) {
        this.orderItem = orderItem;
        this.productId = productId;
    }

    public OrderItemAdditionalJPA() {

    }

    public OrderItemAdditional toEntity() {
        return new OrderItemAdditional(
            id,
            orderItemId,
            productId
        );
    }


    public static OrderItemAdditionalJPA toJPA(Long productId, OrderItemJPA orderItemJPA) {
        return new OrderItemAdditionalJPA(
            orderItemJPA,
            productId
        );
    }

}
