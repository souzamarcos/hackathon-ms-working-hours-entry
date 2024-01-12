package com.fiap.burger.entity.order;

import com.fiap.burger.entity.client.Client;
import com.fiap.burger.entity.common.BaseEntity;
import com.fiap.burger.entity.product.Product;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Order extends BaseEntity {

    private String clientToken;
    private Client client;
    private List<OrderItem> items;
    private Double total;
    private OrderStatus status;
    private OrderPaymentStatus paymentStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(hashCode(), order.hashCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            getId(),
            getClientToken(),
            getItems(),
            getTotal(),
            getStatus(),
            getCreatedAt(),
            getModifiedAt(),
            getDeletedAt()
        );
    }

    @Override
    public String toString() {
        return "{" +
            "clientToken='" + clientToken + '\'' +
            ", client=" + client +
            ", items=" + items +
            ", total=" + total +
            ", status=" + status +
            ", paymentStatus=" + paymentStatus +
            ", id=" + id +
            '}';
    }

    public String getClientToken() {
        return clientToken;
    }

    public Client getClient() {
        return client;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Long> getProductIds() {
        List<Long> productIds = Optional.ofNullable(this.getItems())
            .orElse(Collections.emptyList())
            .stream().map(OrderItem::getProductId).collect(Collectors.toList());

        productIds.addAll(Optional.ofNullable(this.getItems())
            .orElse(Collections.emptyList())
            .stream().flatMap(item ->
                Optional.ofNullable(item.getAdditionalIds())
                    .orElse(
                        Optional.ofNullable(item.getOrderItemAdditionals()).orElse(Collections.emptyList())
                            .stream()
                            .map(OrderItemAdditional::getProductId)
                            .toList()
                    )
                    .stream())
            .toList());

        return productIds;
    }

    public void enrichItemsWithProducts(List<Product> products) {
        Optional.ofNullable(this.getItems()).orElse(Collections.emptyList())
            .forEach((OrderItem i) -> {
                var maybeProduct = products.stream().filter(p -> p.getId().equals(i.getProductId())).findFirst();
                maybeProduct.ifPresent(i::setProduct);

                Optional.ofNullable(i.getOrderItemAdditionals()).orElse(Collections.emptyList()).forEach(a -> {
                    var maybeAdditional = products.stream().filter(p -> p.getId().equals(a.getProductId())).findFirst();
                    maybeAdditional.ifPresent(a::setProduct);
                });
            });
    }

    public Order(String clientId, List<OrderItem> items, OrderStatus status) {
        this.clientToken = clientId;
        this.items = items;
        this.status = status;
    }

    public Order(
        Long id,
        Client client,
        Double total,
        OrderStatus status,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDateTime deletedAt
    ) {
        this.id = id;
        this.client = client;
        this.total = total;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }

    public Order(
        Long id,
        Client client,
        List<OrderItem> items,
        Double total,
        OrderStatus status,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDateTime deletedAt
    ) {
        this.id = id;
        this.client = client;
        this.items = items;
        this.total = total;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }

    public boolean canBePaid() {
        return OrderStatus.AGUARDANDO_PAGAMENTO.equals(this.status);
    }
}
