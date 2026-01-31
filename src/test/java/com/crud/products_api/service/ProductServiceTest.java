package com.crud.products_api.service;

import com.crud.products_api.helper.ProductSpecificationHelper;
import com.crud.products_api.model.dto.request.ProductFilterDTO;
import com.crud.products_api.model.dto.request.ProductRequestDTO;
import com.crud.products_api.model.dto.response.ProductDetailsDTO;
import com.crud.products_api.model.entity.ProductEntity;
import com.crud.products_api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductSpecificationHelper productSpecificationHelper;

    @Captor
    private ArgumentCaptor<ProductEntity> productCaptor;

    @Test
    void shouldCreateProduct() {

        ProductRequestDTO productRequestDTO = new ProductRequestDTO(
                "Test Product Name",
                new BigDecimal("19.90"),
                "Teste Product Description"
        );

        productService.createProduct(productRequestDTO);

        then(productRepository).should().save(productCaptor.capture());
        ProductEntity createdProduct = productCaptor.getValue();
        assertEquals(productRequestDTO.name(), createdProduct.getName());
        assertEquals(productRequestDTO.price(), createdProduct.getPrice());
        assertEquals(productRequestDTO.description(), createdProduct.getDescription());
        assertNotNull(createdProduct.getCreatedAt());
    }

    @Test
    void shouldGetProductById() {

        UUID id = UUID.randomUUID();
        ProductEntity product = new ProductEntity();
        product.setId(id);

        given(productRepository.findById(id)).willReturn(Optional.of(product));

        ProductDetailsDTO result = productService.getProductById(id);

        assertNotNull(result);
    }

    @Test
    void shouldUpdateProduct() {

        UUID id = UUID.randomUUID();
        ProductEntity product = new ProductEntity();
        product.setId(id);

        ProductRequestDTO productRequestDTO = new ProductRequestDTO(
                "Test Product Name",
                new BigDecimal("19.90"),
                "Teste Product Description"
        );

        given(productRepository.findById(id)).willReturn(Optional.of(product));

        productService.updateProduct(id, productRequestDTO);

        assertEquals(productRequestDTO.name(), product.getName());
        assertEquals(productRequestDTO.price(), product.getPrice());
        assertEquals(productRequestDTO.description(), product.getDescription());
    }

    @Test
    void shouldUpdateProductNullValues() {

        UUID id = UUID.randomUUID();
        ProductEntity product = new ProductEntity();
        product.setId(id);
        product.setName("Test Product Name");
        product.setPrice(new BigDecimal("19.90"));
        product.setDescription("Teste Product Description");

        ProductRequestDTO productRequestDTO = new ProductRequestDTO(
                null,
                null,
                null
        );

        given(productRepository.findById(id)).willReturn(Optional.of(product));

        productService.updateProduct(id, productRequestDTO);

        assertNotEquals(productRequestDTO.name(), product.getName());
        assertNotEquals(productRequestDTO.price(), product.getPrice());
        assertNotEquals(productRequestDTO.description(), product.getDescription());
    }

    @Test
    void shouldDeleteProduct() {

        UUID id = UUID.randomUUID();
        ProductEntity product = new ProductEntity();
        product.setId(id);

        productService.deleteProduct(id);

        then(productRepository).should().deleteById(id);
    }

    @Test
    void shouldPageProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        ProductFilterDTO productFilterDTO = new ProductFilterDTO(
                "Teclado",
                null,
                null,
                null,
                null
        );

        Specification<ProductEntity> mockSpec = (root, query, cb) -> null;
        when(productSpecificationHelper.withFilter(productFilterDTO)).thenReturn(mockSpec);

        ProductEntity entity = new ProductEntity();
        entity.setName("Teclado Gamer");
        entity.setPrice(BigDecimal.valueOf(250.00));

        Page<ProductEntity> page = new PageImpl<>(List.of(entity));
        when(productRepository.findAll(mockSpec, pageable)).thenReturn(page);

        Page<ProductDetailsDTO> result = productService.getAllProducts(pageable, productFilterDTO);

        assertAll(
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals("Teclado Gamer", result.getContent().get(0).name()),
                () -> verify(productRepository).findAll(mockSpec, pageable)
        );
    }
}