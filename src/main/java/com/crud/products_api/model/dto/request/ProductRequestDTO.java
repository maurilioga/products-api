package com.crud.products_api.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank
        String name,

        @NotNull
        @DecimalMin(value = "0.00")
        @Digits(integer = 8, fraction = 2, message = "Preço fora do padrão permitido (até 8 dígitos inteiros e 2 decimais)")
        BigDecimal price,

        String description
) {
}
