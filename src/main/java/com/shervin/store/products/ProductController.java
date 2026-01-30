package com.shervin.store.products;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
@Tag(name="Products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public List<ProductDto> AllProducts(@RequestParam(required = false, name = "categoryId") Byte categoryId) {

        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findAllByCategoryId(categoryId);
        } else {
            products = productRepository.findAllWithCategory();
        }
        return products.stream()
                .map(productMapper::toDto).toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {

        var productDto = productRepository.findById(id).orElse(null);
        if  (productDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(productDto));
    }

    @PostMapping
    public ResponseEntity<ProductDto> CreateProduct(
            @RequestBody ProductDto request,
            UriComponentsBuilder uriBuilder
            ){
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        var product = productMapper.toEntity(request);
        product.setCategory(category);
        productRepository.save(product);
        request.setId(product.getId());
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();

        return ResponseEntity.created(uri).body(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DeleteProduct(@PathVariable Long id){
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> UpdateProduct(
            @RequestBody ProductDto productDto,
            @PathVariable Long id){

        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        var product =  productRepository.findById(id).orElse(null);
        if (product == null){
            return ResponseEntity.notFound().build();
        }

        productMapper.update(productDto, product);
        product.setCategory(category);
        productDto.setId(id);

        return ResponseEntity.ok(productDto);


    }

    @PostMapping("/{id}/update-price")
    public ResponseEntity<Void> UpdateProductPrice(
            @PathVariable Long id,
            @RequestBody(required = false) ChangePriceReqDto request
            ){
            var product =  productRepository.findById(id).orElse(null);
            if (product == null) {
                return ResponseEntity.notFound().build();
            }
            if (!request.getOldPrice().equals(product.getPrice())) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
            product.setPrice(request.getNewPrice());
            productRepository.save(product);
            return ResponseEntity.ok().build();
    }





}
