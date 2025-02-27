package com.maghrebia.data_extract.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.maghrebia.data_extract.DAO.Entities.Contacts;
import com.maghrebia.data_extract.DTO.ContactsDTO;

@Mapper(componentModel = "spring")
public interface ContactsMapper {

    ContactsMapper INSTANCE = Mappers.getMapper(ContactsMapper.class);

    @Mapping(target = "idContact", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "contracts", ignore =  true)
    Contacts toEntity(ContactsDTO dto); 

    ContactsDTO toDTO(Contacts contact);
}
