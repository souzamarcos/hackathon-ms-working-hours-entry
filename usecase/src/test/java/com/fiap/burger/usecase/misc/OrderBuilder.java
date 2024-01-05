package com.fiap.burger.usecase.misc;

import com.fiap.burger.entity.client.Client;
import com.fiap.burger.entity.order.Order;
import com.fiap.burger.entity.order.OrderItem;
import com.fiap.burger.entity.order.OrderItemAdditional;
import com.fiap.burger.entity.order.OrderStatus;
import com.fiap.burger.entity.product.Category;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderBuilder {
    //chave gerada com secret "TEST-SECRET" e issuer "TEST-ISSUER" sem expiração
    public static final String CLIENT_TOKEN_ID_1L = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBdWRpZW5jZSIsImNsaWVudElkIjoxLCJpc3MiOiJURVNULUlTU1VFUiIsImNwZiI6IjE2NTY1ODI0NzM4IiwiaWF0IjoxNjk4NjI1OTYxLCJqdGkiOiJhNzIyYTBiNC1lY2M5LTQ2ZDQtOTRhYy00Mzg1NzI1YTAxOTcifQ.CuXTgz2VE-5ThjQRHMQtZ3iZE5zz3JV0vji5urdqrPI";

    private Long id = 1L;

    private Client client = new ClientBuilder().build();

    private List<OrderItem> items = List.of(new OrderItem(1L, id, List.of(new OrderItemAdditional(2L, 1L, new ProductBuilder().withId(2L).withCategory(Category.ADICIONAL).build())), "Comentário", new ProductBuilder().withId(1L).build()));

    private Double total = 40.44;

    private OrderStatus status = OrderStatus.AGUARDANDO_PAGAMENTO;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime modifiedAt = LocalDateTime.now();

    private LocalDateTime deletedAt = null;

    private String clientToken = CLIENT_TOKEN_ID_1L;

    public OrderBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder withClient(Client client) {
        this.client = client;
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

    public OrderBuilder withClientToken(String clientToken) {
        this.clientToken = clientToken;
        return this;
    }

    public Order build() {
        return new Order(id, client, items, total, status, createdAt, modifiedAt, deletedAt);
    }

    public Order toInsert() {
        return new Order(
            Optional.ofNullable(client).map((Client client) -> clientToken).orElse(null),
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
