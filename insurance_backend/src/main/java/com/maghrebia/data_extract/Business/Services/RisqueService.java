package com.maghrebia.data_extract.Business.Services;

import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Sheet;

import com.maghrebia.data_extract.DAO.Entities.Risque;
import com.maghrebia.data_extract.DTO.RisqueDTO;

public interface RisqueService {
    
    public void importRisques(Sheet sheet);
    public Optional<Risque> findBycodeRisque(Integer codeRisque);
    public List<RisqueDTO> getAllRisques();
    public void addRisque(RisqueDTO risqueDTO);
    public void upddateRisque(Long risqueID, RisqueDTO risqueDTO);
    public RisqueDTO getRisqueByID(Long risqueId);
    public void deleteRisk(Long risqueID);
}
