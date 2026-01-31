package com.crud.products_api.model.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductFilterDTO(
        String name,
        BigDecimal priceMin,
        BigDecimal priceMax,
        LocalDate createdAtFrom,
        LocalDate createdAtTo
) {
}
