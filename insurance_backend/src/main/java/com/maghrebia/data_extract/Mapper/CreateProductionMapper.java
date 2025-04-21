package com.maghrebia.data_extract.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.maghrebia.data_extract.DAO.Entities.Production;
import com.maghrebia.data_extract.DTO.CreateProductionDTO;

@Mapper(componentModel = "spring")
public interface CreateProductionMapper {

    CreateProductionMapper INSTANCE = Mappers.getMapper(CreateProductionMapper.class);

    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "contact", ignore = true)
    @Mapping(target = "risque", ignore = true)
    @Mapping(target = "idProduction", ignore = true)
    Production toEntity(CreateProductionDTO createProductionDTO);

    @Mapping(target = "codeRisque", ignore = true)
    CreateProductionDTO toDto(Production production);

}
