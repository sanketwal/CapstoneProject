package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.ProductDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping("/products/{id}")
    public ProductDto getProductById(@PathVariable Long id){
        ProductDto product = new ProductDto();
        product.setId(id);
        return product;
    }

    @PostMapping("/products")
    public ProductDto createProduct(@RequestBody ProductDto product){
        return product;
    }
}
