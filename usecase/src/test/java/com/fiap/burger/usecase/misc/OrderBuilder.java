package com.fiap.burger.usecase.misc;

import com.fiap.burger.entity.customer.Customer;
import com.fiap.burger.entity.order.Order;
import com.fiap.burger.entity.order.OrderItem;
import com.fiap.burger.entity.order.OrderItemAdditional;
import com.fiap.burger.entity.order.OrderStatus;
import com.fiap.burger.entity.product.Category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderBuilder {
    //chave gerada com secret "TEST-SECRET" e issuer "TEST-ISSUER" sem expiração
    private static final String CUSTOMER_TOKEN_ID_1 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBdWRpZW5jZSIsImlzcyI6IlRFU1QtSVNTVUVSIiwiY3VzdG9tZXJJZCI6IjEiLCJjcGYiOiIxMjM0NTY3ODkwMSIsImlhdCI6MTcwNjQxMzQzMiwianRpIjoiZjA0MjNlNGMtN2MxMC00YjM1LThkOTUtZGJmYWZjM2NmZGE0In0.ofm4-HItY30TdyIzbqGgo-KXZf-OIlwhQckryBt52S8";

    private Long id = 1L;

    private Customer customer = new CustomerBuilder().build();

    private List<OrderItem> items = List.of(new OrderItem(1L, id, List.of(new OrderItemAdditional(2L, 1L, new ProductBuilder().withId(2L).withCategory(Category.ADICIONAL).build())), "Comentário", new ProductBuilder().withId(1L).build()));

    private Double total = 40.44;

    private OrderStatus status = OrderStatus.AGUARDANDO_PAGAMENTO;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime modifiedAt = LocalDateTime.now();

    private LocalDateTime deletedAt = null;

    private String customerToken = CUSTOMER_TOKEN_ID_1;

    public OrderBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public OrderBuilder withItems(List<OrderItem> items) {
        this.items = items;
        return this;
    }

    public OrderBuilder withTotal(Double total) {
        this.total = total;
        return this;
    }

    public OrderBuilder withStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderBuilder withModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public OrderBuilder withCustomerToken(String customerToken) {
        this.customerToken = customerToken;
        return this;
    }

    public Order build() {
        return new Order(id, customer, items, total, status, createdAt, modifiedAt, deletedAt);
    }

    public Order toInsert() {
        return new Order(
            Optional.ofNullable(customer).map((Customer customer) -> customerToken).orElse(null),
            Optional.ofNullable(items).map(this::toInsertOrderItems).orElse(null),
            status);
    }

    private List<OrderItem> toInsertOrderItems(List<OrderItem> items) {
        return items.stream().map(this::toInsertOrderItem).collect(Collectors.toList());
    }

    private OrderItem toInsertOrderItem(OrderItem i) {
        return new OrderItem(i.getId(), i.getOrderId(), i.getProduct().getId(), i.getOrderItemAdditionals().stream().map(a -> a.getProduct().getId()).collect(Collectors.toList()), i.getComment());
    }
}
