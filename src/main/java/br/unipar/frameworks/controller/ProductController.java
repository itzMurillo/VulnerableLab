package br.unipar.frameworks.controller;

import br.unipar.frameworks.dto.ProductRequest;
import br.unipar.frameworks.dto.ProductResponse;
import br.unipar.frameworks.model.Product;
import br.unipar.frameworks.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<ProductResponse> listProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        Product product = toEntity(request);
        return ResponseEntity.ok(ProductResponse.from(productRepository.save(product)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request
    ) {
        productRepository.findById(id).orElseThrow();
        Product product = toEntity(request);
        product.setId(id);
        return ResponseEntity.ok(ProductResponse.from(productRepository.save(product)));
    }

    private Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        return product;
    }
}
