package com.coderati.microservice.order.model.mapper;

import com.coderati.microservice.order.dto.OrderRequest;
import com.coderati.microservice.order.dto.OrderResponse;
import com.coderati.microservice.order.model.entity.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "codeProduct", source = "codeProduct")
    OrderModel requestToModel(OrderRequest order);

    @Mapping(target = "status", source = "statusOrder", /*expression = "java.trim()",*/ qualifiedByName = "mapStatus")
    @Mapping(target = "createdAt", source = "dateOrder")
    OrderResponse modelToResponse(OrderModel orderModel);

    @Named("mapStatus")
    default OrderResponse.StatusEnum mapStatus(String status) {
        return OrderResponse.StatusEnum.fromValue(status);
    }
}
