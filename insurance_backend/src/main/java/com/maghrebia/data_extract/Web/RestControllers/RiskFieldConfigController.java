package com.maghrebia.data_extract.Web.RestControllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.maghrebia.data_extract.Business.ServicesImpl.RiskFieldConfigServiceImpl;
import com.maghrebia.data_extract.DAO.Entities.RiskFieldConfig;
import com.maghrebia.data_extract.DTO.RiskFieldConfigDto;

@RestController
@RequestMapping("/api/risk-field-config")
public class RiskFieldConfigController {

    private final RiskFieldConfigServiceImpl riskFieldConfigServiceImpl;

    public RiskFieldConfigController(RiskFieldConfigServiceImpl riskFieldConfigServiceImpl) {
        this.riskFieldConfigServiceImpl = riskFieldConfigServiceImpl;
    }

    // Endpoint to create or update RiskFieldConfig
    @PostMapping
    public ResponseEntity<RiskFieldConfig> addRiskFieldConfig(@RequestBody RiskFieldConfigDto dto) {
        RiskFieldConfig savedConfig = riskFieldConfigServiceImpl.saveRiskFieldConfig(dto);
        return new ResponseEntity<>(savedConfig, HttpStatus.CREATED);
    }

    // Endpoint to get the RiskFieldConfig by Risque ID
    @GetMapping("/{risqueId}")
    public ResponseEntity<RiskFieldConfig> getRiskFieldConfig(@PathVariable Long risqueId) {
        RiskFieldConfig config = riskFieldConfigServiceImpl.getRiskFieldConfigByRisque(risqueId);
        return new ResponseEntity<>(config, HttpStatus.OK);
    }

    // Endpoint to delete RiskFieldConfig
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRiskFieldConfig(@PathVariable Long id) {
        riskFieldConfigServiceImpl.deleteRiskFieldConfig(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
