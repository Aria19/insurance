package com.maghrebia.data_extract.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.maghrebia.data_extract.DAO.Entities.Banque;
import com.maghrebia.data_extract.DTO.CreateBanqueDTO;

@Mapper(componentModel = "spring")
public interface CreateBanqueMapper {

    CreateBanqueMapper INSTANCE = Mappers.getMapper(CreateBanqueMapper.class);

    @Mapping(target = "idTransaction", ignore = true)
    @Mapping(target = "contact", ignore = true)
    @Mapping(target = "contract", ignore = true)
    Banque toEntity(CreateBanqueDTO dto);

    @Mapping(target = "contactName", ignore = true)
    CreateBanqueDTO toDto(Banque banque);

}
