package com.maghrebia.data_extract.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.maghrebia.data_extract.DAO.Entities.Risque;
import com.maghrebia.data_extract.DTO.RisqueDTO;

@Mapper(componentModel = "spring")
public interface RisqueMapper {

    RisqueMapper INSTANCE = Mappers.getMapper(RisqueMapper.class);

    // Map DTO to Entity
    default Risque toEntity(RisqueDTO dto) {
        if (dto == null) return null;
        Risque r = new Risque(); // use no-args constructor
        r.setIdRisque(dto.getIdRisque());
        r.setCodeRisque(dto.getCodeRisque());
        r.setRisqueName(dto.getRisqueName());
        r.setCommission(dto.getCommission());
        return r;
    }

    // Map Entity to DTO
    default RisqueDTO toDTO(Risque entity) {
        if (entity == null) return null;
        return new RisqueDTO(entity); // use your existing constructor
    }
}
