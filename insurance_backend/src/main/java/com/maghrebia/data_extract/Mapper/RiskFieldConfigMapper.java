package com.maghrebia.data_extract.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.maghrebia.data_extract.DAO.Entities.RiskFieldConfig;
import com.maghrebia.data_extract.DTO.RiskFieldConfigDto;

@Mapper(componentModel = "spring")
public interface RiskFieldConfigMapper {

    RiskFieldConfigMapper INSTANCE = Mappers.getMapper(RiskFieldConfigMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "risque", ignore = true)
    RiskFieldConfig toEntity(RiskFieldConfigDto riskFieldConfigDto);

    @Mapping(target = "risqueId", ignore = true)
    RiskFieldConfigDto toDTO(RiskFieldConfig riskFieldConfig);

}
