package com.maghrebia.data_extract.Business.ServicesImpl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.maghrebia.data_extract.DAO.Entities.RiskFieldConfig;
import com.maghrebia.data_extract.DAO.Entities.Risque;
import com.maghrebia.data_extract.DAO.Repositories.RiskFieldConfigRepository;
import com.maghrebia.data_extract.DAO.Repositories.RisqueRepository;
import com.maghrebia.data_extract.DTO.RiskFieldConfigDto;
import com.maghrebia.data_extract.exceptions.ResourceNotFoundException;

@Service
public class RiskFieldConfigServiceImpl {
    
    private final RiskFieldConfigRepository riskFieldConfigRepository;
    private final RisqueRepository risqueRepository; // To fetch the 'Risque' entity

    public RiskFieldConfigServiceImpl(RiskFieldConfigRepository riskFieldConfigRepository,
            RisqueRepository risqueRepository) {
        this.riskFieldConfigRepository = riskFieldConfigRepository;
        this.risqueRepository = risqueRepository;
    }

    // Create or update RiskFieldConfig
    public RiskFieldConfig saveRiskFieldConfig(RiskFieldConfigDto dto) {
        Optional<Risque> risque = risqueRepository.findById(dto.getRisqueId());

        if (!risque.isPresent()) {
            throw new ResourceNotFoundException("Risque not found");
        }

        RiskFieldConfig riskFieldConfig = new RiskFieldConfig();
        riskFieldConfig.setRisque(risque.get());
        riskFieldConfig.setRequiredFields(dto.getRequiredFields());

        return riskFieldConfigRepository.save(riskFieldConfig);
    }

    // Retrieve RiskFieldConfig by Risque
    public RiskFieldConfig getRiskFieldConfigByRisque(Long risqueId) {
        Optional<Risque> risque = risqueRepository.findById(risqueId);

        if (!risque.isPresent()) {
            throw new ResourceNotFoundException("Risque not found");
        }

        return riskFieldConfigRepository.findByRisque(risque.get())
                .orElseThrow(() -> new ResourceNotFoundException("RiskFieldConfig not found for this Risque"));
    }

    // Optional: Delete a RiskFieldConfig
    public void deleteRiskFieldConfig(Long id) {
        riskFieldConfigRepository.deleteById(id);
    }
}
