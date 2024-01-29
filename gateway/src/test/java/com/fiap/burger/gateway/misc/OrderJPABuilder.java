package com.fiap.burger.gateway.misc;

import com.fiap.burger.entity.customer.Customer;
import com.fiap.burger.entity.order.OrderStatus;
import com.fiap.burger.gateway.order.model.OrderItemJPA;
import com.fiap.burger.gateway.order.model.OrderJPA;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class OrderJPABuilder {

    private Long id = 1L;

    private Customer customer = new CustomerBuilder().build();

    private List<OrderItemJPA> items = Collections.emptyList();

    private Double total = 40.44;

    private OrderStatus status = OrderStatus.AGUARDANDO_PAGAMENTO;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime modifiedAt = LocalDateTime.now();

    private LocalDateTime deletedAt = null;

    public OrderJPABuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderJPABuilder withClient(Customer customer) {
        this.customer = customer;
        return this;
    }

    public OrderJPABuilder withItems(List<OrderItemJPA> items) {
        this.items = items;
        return this;
    }

    public OrderJPABuilder withTotal(Double total) {
        this.total = total;
        return this;
    }

    public OrderJPABuilder withStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderJPABuilder withModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public OrderJPA build() {
        return new OrderJPA(id, customer.getId(), items, total, status, createdAt, modifiedAt, deletedAt);
    }

}
