package com.maghrebia.data_extract.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.maghrebia.data_extract.DAO.Entities.Production;
import com.maghrebia.data_extract.DTO.ProductionDTO;

@Mapper(componentModel = "spring")
public interface ProductionMapper {

    ProductionMapper INSTANCE = Mappers.getMapper(ProductionMapper.class);

    Production toEntity(ProductionDTO productionDTO);

    ProductionDTO toDto(Production production);
}
