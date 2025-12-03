package com.coderati.microservice.order.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table(name = "order_shop")
public class OrderModel {
    @Id
    private Integer id;
    private String codeProduct;
    private String dateOrder;
    private String statusOrder;
}
