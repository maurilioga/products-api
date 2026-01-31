package com.crud.products_api.controller;

import com.crud.products_api.model.dto.request.ProductFilterDTO;
import com.crud.products_api.model.dto.request.ProductRequestDTO;
import com.crud.products_api.model.dto.response.ProductCreatedDTO;
import com.crud.products_api.model.dto.response.ProductDetailsDTO;
import com.crud.products_api.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<ProductRequestDTO> productRequestDTO;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateProductAndReturn201() throws Exception {

        ProductRequestDTO productDTO = new ProductRequestDTO(
                "Test Product Name",
                new BigDecimal("19.90"),
                "Teste Product Description"
        );

        ProductCreatedDTO createdProduct = new ProductCreatedDTO(
                UUID.randomUUID(),
                "Test Product Name",
                new BigDecimal("19.90"),
                "Teste Product Description",
                LocalDateTime.now()
        );

        given(productService.createProduct(any()))
                .willReturn(createdProduct);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void shouldGetProductAndReturn200() throws Exception {

        UUID id = UUID.randomUUID();

        ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO(
                id,
                "Test Product Name",
                new BigDecimal("19.90"),
                "Teste Product Description",
                LocalDateTime.now(),
                null
        );

        given(productService.getProductById(id))
                .willReturn(productDetailsDTO);

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateProductAndReturn204() throws Exception {

        UUID id = UUID.randomUUID();

        ProductRequestDTO productDTO = new ProductRequestDTO(
                "Test Product Name",
                new BigDecimal("19.90"),
                "Teste Product Description"
        );

        willDoNothing()
                .given(productService)
                .updateProduct(eq(id), any(ProductRequestDTO.class));

        mockMvc.perform(put("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteProductAndReturn204() throws Exception {

        UUID id = UUID.randomUUID();

        willDoNothing()
                .given(productService)
                .deleteProduct(id);

        mockMvc.perform(delete("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetAllProductAndReturn200() throws Exception {

        UUID id = UUID.randomUUID();

        ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO(
                id,
                "Test Product Name",
                new BigDecimal("19.90"),
                "Teste Product Description",
                LocalDateTime.now(),
                null
        );

        Page<ProductDetailsDTO> page = new PageImpl<>(
                List.of(productDetailsDTO),
                PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt", "id")),
                1
        );

        when(productService.getAllProducts(any(Pageable.class), any(ProductFilterDTO.class)))
                .thenReturn(page);

        mockMvc.perform(get("/products")
                                .param("page", "0")
                                .param("size", "20")
                )
                .andExpect(status().isOk());
    }
}