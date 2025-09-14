package com.maghrebia.data_extract.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.maghrebia.data_extract.DAO.Entities.Risque;
import com.maghrebia.data_extract.DTO.RisqueDTO;

@Mapper(componentModel = "spring")
public interface RisqueMapper {

    RisqueMapper INSTANCE = Mappers.getMapper(RisqueMapper.class);

    Risque toEntity(RisqueDTO risqueDTO);

    RisqueDTO toDTO(Risque risque);
}
