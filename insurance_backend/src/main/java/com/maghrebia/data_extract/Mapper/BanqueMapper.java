package com.maghrebia.data_extract.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.maghrebia.data_extract.DAO.Entities.Banque;
import com.maghrebia.data_extract.DTO.BanqueDTO;

@Mapper(componentModel = "spring")
public interface BanqueMapper {

    BanqueMapper INSTANCE = Mappers.getMapper(BanqueMapper.class);

    @Mapping(target = "idTransaction", ignore = true)
    @Mapping(target = "contact", ignore = true)
    @Mapping(target = "contract", ignore = true)
    Banque toEntity(BanqueDTO dto);

    @Mapping(target = "contactName", ignore = true)
    @Mapping(target = "numeroContrat", ignore = true)
    @Mapping(target = "codeRisque", ignore = true)
    BanqueDTO toDto(Banque banque);
}
