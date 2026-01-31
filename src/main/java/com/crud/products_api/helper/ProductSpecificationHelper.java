package com.crud.products_api.helper;

import com.crud.products_api.model.dto.request.ProductFilterDTO;
import com.crud.products_api.model.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ProductSpecificationHelper {

    public Specification<ProductEntity> withFilter(ProductFilterDTO productFilter) {
        return Specification
                .where(nameFilter(productFilter.name()))
                .and(priceMinFilter(productFilter.priceMin()))
                .and(priceMaxFilter(productFilter.priceMax()))
                .and(createdAtFromFilter(productFilter.createdAtFrom()))
                .and(createdAtToFilter(productFilter.createdAtTo()));
    }

    private static Specification<ProductEntity> nameFilter(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.upper(root.get("name")), "%" + name.toUpperCase() + "%");
    }

    private static Specification<ProductEntity> priceMinFilter(BigDecimal priceMin) {
        return (root, query, cb) ->
                priceMin == null ? null : cb.greaterThanOrEqualTo(root.get("price"), priceMin);
    }

    private static Specification<ProductEntity> priceMaxFilter(BigDecimal priceMax) {
        return (root, query, cb) ->
                priceMax == null ? null : cb.lessThanOrEqualTo(root.get("price"), priceMax);
    }

    private static Specification<ProductEntity> createdAtFromFilter(LocalDate from) {
        return (root, query, cb) ->
                from == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), from.minusDays(1).atStartOfDay());
    }

    private static Specification<ProductEntity> createdAtToFilter(LocalDate to) {
        return (root, query, cb) ->
                to == null ? null : cb.lessThanOrEqualTo(root.get("createdAt"), to.plusDays(1).atStartOfDay());
    }
}
