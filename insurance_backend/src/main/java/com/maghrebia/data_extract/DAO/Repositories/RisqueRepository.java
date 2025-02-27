package com.maghrebia.data_extract.DAO.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maghrebia.data_extract.DAO.Entities.Risque;
import java.util.Optional;


@Repository
public interface RisqueRepository extends JpaRepository<Risque, Long> {

    Optional<Risque> findBycodeRisque(Integer codeRisque);
}
