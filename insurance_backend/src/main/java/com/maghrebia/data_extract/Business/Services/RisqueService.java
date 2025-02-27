package com.maghrebia.data_extract.Business.Services;

import java.util.Optional;

import org.apache.poi.ss.usermodel.Sheet;

import com.maghrebia.data_extract.DAO.Entities.Risque;

public interface RisqueService {
    
    public void importRisques(Sheet sheet);
    public Optional<Risque> findBycodeRisque(Integer codeRisque);
}
