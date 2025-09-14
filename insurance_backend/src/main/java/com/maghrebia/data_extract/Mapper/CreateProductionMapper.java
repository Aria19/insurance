package com.maghrebia.data_extract.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.maghrebia.data_extract.DAO.Entities.Production;
import com.maghrebia.data_extract.DTO.CreateProductionDTO;

@Mapper(componentModel = "spring")
public interface CreateProductionMapper {

    CreateProductionMapper INSTANCE = Mappers.getMapper(CreateProductionMapper.class);

    Production toEntity(CreateProductionDTO createProductionDTO);

    CreateProductionDTO toDto(Production production);

}
