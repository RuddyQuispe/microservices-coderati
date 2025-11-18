package com.coderati.microservice.inventory.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table(name = "products")
public class ProductModel {
    @Id
    private Integer id;
    private String code;
    @Column("nameProduct")
    private String nameProduct;
    private Integer stock;
    private Double price;

}
