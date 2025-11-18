package com.coderati.microservice.inventory.model.mapper;

import com.coderati.microservice.inventory.dto.InventoryRequest;
import com.coderati.microservice.inventory.dto.InventoryResponse;
import com.coderati.microservice.inventory.model.entity.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventoryMapper {

    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

    @Mapping(target = "code", source = "idProduct")
    ProductModel requestToModel(InventoryRequest request);

    @Mapping(target = "idProduct", source = "code")
    InventoryResponse modelToResponse(ProductModel model);

}
