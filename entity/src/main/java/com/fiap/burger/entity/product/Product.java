package com.fiap.burger.entity.product;

import com.fiap.burger.entity.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Product extends BaseEntity {
    private Category category;
    private String name;
    private String description;
    private Double value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(hashCode(), product.hashCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            getId(),
            getCategory(),
            getName(),
            getDescription(),
            getValue(),
            getCreatedAt(),
            getModifiedAt(),
            getDeletedAt()
        );
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getValue() {
        return value;
    }

    public Product() {

    }

    public Product(Category category, String name, String description, Double value) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public Product(Long id, Category category, String name, String description, Double value) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public Product(
        Long id,
        Category category,
        String name,
        String description,
        Double value,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDateTime deletedAt
    ) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description;
        this.value = value;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }
}
