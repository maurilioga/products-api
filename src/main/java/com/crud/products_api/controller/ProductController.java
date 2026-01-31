package com.crud.products_api.controller;

import com.crud.products_api.model.dto.request.ProductFilterDTO;
import com.crud.products_api.model.dto.request.ProductRequestDTO;
import com.crud.products_api.model.dto.response.ProductDetailsDTO;
import com.crud.products_api.model.dto.response.ProductCreatedDTO;
import com.crud.products_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.validation.Valid;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductCreatedDTO> create(@RequestBody @Valid ProductRequestDTO productRequestDTO,
                                                    UriComponentsBuilder uriComponentsBuilder) {

        ProductCreatedDTO productCreatedDTO = productService.createProduct(productRequestDTO);
        var uri = uriComponentsBuilder.path("/products/{id}").buildAndExpand(productCreatedDTO.id()).toUri();

        return ResponseEntity.created(uri).body(productCreatedDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsDTO> getById(@PathVariable UUID id) {

        ProductDetailsDTO productDetailsDTO = productService.getProductById(id);
        return ResponseEntity.ok().body(productDetailsDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailsDTO> update(@PathVariable UUID id,
                                                    @RequestBody @Valid ProductRequestDTO productRequestDTO) {

        productService.updateProduct(id, productRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDetailsDTO> delete(@PathVariable UUID id) {

        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductDetailsDTO>> getAll(@PageableDefault(size = 20,
                                                              sort = {"createdAt", "id"},
                                                              direction = Sort.Direction.DESC) Pageable pageable,

                                                          @ModelAttribute ProductFilterDTO productFilterDTO) {

        Page<ProductDetailsDTO> productDetailsDTO = productService.getAllProducts(pageable, productFilterDTO);
        return ResponseEntity.ok().body(productDetailsDTO);
    }
}
