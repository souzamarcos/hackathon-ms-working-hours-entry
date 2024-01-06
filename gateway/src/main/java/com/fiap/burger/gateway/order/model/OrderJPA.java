package com.fiap.burger.gateway.order.model;

import com.fiap.burger.entity.client.Client;
import com.fiap.burger.entity.order.Order;
import com.fiap.burger.entity.order.OrderPaymentStatus;
import com.fiap.burger.entity.order.OrderStatus;
import com.fiap.burger.gateway.misc.common.BaseDomainJPA;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "`order`")
public class OrderJPA extends BaseDomainJPA {

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    List<OrderItemJPA> items;

    @Column
    Double total;
    @Enumerated(EnumType.ORDINAL)
    @Column
    OrderStatus status;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "payment_status")
    OrderPaymentStatus paymentStatus;

    @Column(name = "client_id")
    Long clientId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderJPA orderJPA = (OrderJPA) o;
        return Objects.equals(items, orderJPA.items) && Objects.equals(total, orderJPA.total) && status == orderJPA.status && Objects.equals(clientId, orderJPA.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, total, status, clientId);
    }

    public OrderJPA() {
    }

    public OrderJPA(
        Long id,
        Long clientId,
        List<OrderItemJPA> items,
        Double total,
        OrderStatus status,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDateTime deletedAt

    ) {
        this.id = id;
        this.clientId = clientId;
        this.total = total;
        this.items = items;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }

    public Order toEntity() {
        return new Order(
            id,
            //TODO
            null,
            total,
            status,
            createdAt,
            modifiedAt,
            deletedAt
        );
    }

    public Order toEntityWithItems() {
        return new Order(
            id,
            //TODO
            null,
            items.stream().map(OrderItemJPA::toEntityWithAdditional).toList(),
            total,
            status,
            createdAt,
            modifiedAt,
            deletedAt
        );
    }

    public static OrderJPA toJPA(Order order) {
        OrderJPA newOrder = new OrderJPA(
            order.getId(),
            Optional.ofNullable(order.getClient()).map(Client::getId).orElse(null),
            null,
            order.getTotal(),
            order.getStatus(),
            order.getCreatedAt(),
            order.getModifiedAt(),
            order.getDeletedAt()
        );

        if (!Optional.ofNullable(order.getItems()).orElse(Collections.emptyList()).isEmpty()) {
            List<OrderItemJPA> items = order.getItems().stream().map(orderItem -> OrderItemJPA.toJPA(orderItem, newOrder)).toList();
            newOrder.setItems(items);
        }
        return newOrder;
    }

    public void setItems(List<OrderItemJPA> items) {
        this.items = items;
    }
}