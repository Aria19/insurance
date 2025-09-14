package com.maghrebia.data_extract.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.maghrebia.data_extract.DAO.Entities.Banque;
import com.maghrebia.data_extract.DTO.BanqueDTO;

@Mapper(componentModel = "spring")
public interface BanqueMapper {

    BanqueMapper INSTANCE = Mappers.getMapper(BanqueMapper.class);

    Banque toEntity(BanqueDTO dto);

    BanqueDTO toDto(Banque banque);
}
