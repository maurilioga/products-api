package com.crud.products_api.model.entity;

import com.crud.products_api.model.dto.request.ProductRequestDTO;
import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "tb_product")
public class ProductEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ProductEntity(ProductRequestDTO productRequestDTO) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.name = productRequestDTO.name();
        this.price = productRequestDTO.price();
        this.description = productRequestDTO.description();
        this.createdAt = LocalDateTime.now();
    }

    public void update(ProductRequestDTO productRequestDTO) {
        if (productRequestDTO.name() != null) {
            this.name = productRequestDTO.name();
        }

        if (productRequestDTO.price() != null) {
            this.price = productRequestDTO.price();
        }

        if (productRequestDTO.description() != null) {
            this.description = productRequestDTO.description();
        }

        this.updatedAt = LocalDateTime.now();
    }
}
