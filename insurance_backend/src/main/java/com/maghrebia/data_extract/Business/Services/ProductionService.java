package com.maghrebia.data_extract.Business.Services;

import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Sheet;

import com.maghrebia.data_extract.DAO.Entities.Production;
import com.maghrebia.data_extract.DTO.ProductionDTO;

public interface ProductionService {

    public void importProduction(Sheet sheet);
    Optional<Production> findByNumeroContrat(String numeroContrat);
    public List<ProductionDTO> getAllProductions();
    public List<ProductionDTO> searchContracts(String keyword, String risk, Integer code);
}
