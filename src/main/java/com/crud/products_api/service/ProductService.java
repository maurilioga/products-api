package com.crud.products_api.service;

import com.crud.products_api.helper.ProductSpecificationHelper;
import com.crud.products_api.model.dto.request.ProductFilterDTO;
import com.crud.products_api.model.dto.request.ProductRequestDTO;
import com.crud.products_api.model.dto.response.ProductDetailsDTO;
import com.crud.products_api.model.dto.response.ProductCreatedDTO;
import com.crud.products_api.model.entity.ProductEntity;
import com.crud.products_api.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductSpecificationHelper productSpecificationHelper;

    @Transactional
    public ProductCreatedDTO createProduct(ProductRequestDTO productRequestDTO) {

        ProductEntity product = new ProductEntity(productRequestDTO);
        productRepository.save(product);

        return new ProductCreatedDTO(product);
    }

    public ProductDetailsDTO getProductById(UUID id) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado!"));

        return new ProductDetailsDTO(product);
    }

    @Transactional
    public void updateProduct(UUID id, ProductRequestDTO productRequestDTO) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado!"));

        product.update(productRequestDTO);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    public Page<ProductDetailsDTO> getAllProducts(Pageable pageable, ProductFilterDTO productFilterDTO) {

        Specification<ProductEntity> spec = productSpecificationHelper.withFilter(productFilterDTO);
        Page<ProductEntity> productPage = productRepository.findAll(spec, pageable);

        return productPage.map(ProductDetailsDTO::new);
    }
}
