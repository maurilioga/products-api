package com.crud.products_api.model.dto.response;

import org.springframework.validation.FieldError;

public record ValidationErrorDTO(String campo, String mensagem) {

    public ValidationErrorDTO(FieldError fieldError) {
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
