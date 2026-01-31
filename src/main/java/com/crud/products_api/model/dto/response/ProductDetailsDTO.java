package com.crud.products_api.model.dto.response;

import com.crud.products_api.model.entity.ProductEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductDetailsDTO(
        UUID id,
        String name,
        BigDecimal price,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public ProductDetailsDTO(ProductEntity product) {
        this(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
