package com.example.productcatalogservice.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product extends BaseModel{
    private Long id;
    private String name;
    private Double price;
    private String imageUrl;
    private String description;
    private Category category;

    private Boolean isPublic;

}
