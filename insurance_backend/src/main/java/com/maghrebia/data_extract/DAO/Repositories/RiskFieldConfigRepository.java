package com.maghrebia.data_extract.DAO.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maghrebia.data_extract.DAO.Entities.RiskFieldConfig;
import com.maghrebia.data_extract.DAO.Entities.Risque;

@Repository
public interface RiskFieldConfigRepository extends JpaRepository<RiskFieldConfig, Long> {

    // Optional: Add custom queries if needed
    // e.g., Find RiskFieldConfig by Risk Code (Risque)
    Optional<RiskFieldConfig> findByRisque(Risque risque);
}