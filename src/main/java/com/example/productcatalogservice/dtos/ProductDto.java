package com.example.productcatalogservice.dtos;

import com.example.productcatalogservice.models.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String name;
    private Double price;
    private String imageUrl;
    private String description;;
    private Category category;
}
