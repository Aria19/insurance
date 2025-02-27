package com.maghrebia.data_extract.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.maghrebia.data_extract.DAO.Entities.Production;
import com.maghrebia.data_extract.DTO.ProductionDTO;

@Mapper(componentModel = "spring")
public interface ProductionMapper {

    ProductionMapper INSTANCE = Mappers.getMapper(ProductionMapper.class);

    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "contact", ignore = true)
    @Mapping(target = "risque", ignore = true)
    @Mapping(target = "idProduction", ignore = true)
    Production toEntity(ProductionDTO productionDTO);

    @Mapping(target = "contactName", ignore = true)
    @Mapping(target = "risqueName", ignore = true)
    @Mapping(target = "codeRisque", ignore = true)
    ProductionDTO toDto(Production production);
}
