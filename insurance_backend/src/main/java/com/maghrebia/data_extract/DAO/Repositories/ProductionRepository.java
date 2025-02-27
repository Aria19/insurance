package com.maghrebia.data_extract.DAO.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maghrebia.data_extract.DAO.Entities.Production;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {

    boolean existsByNumeroContrat(String numeroContrat);

    Optional<Production> findByNumeroContrat(String numeroContrat);

    @Query("SELECT p FROM Production p WHERE " +
            "(:keyword IS NULL OR LOWER(p.contact.assure) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.contact.msh) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:risque IS NULL OR LOWER(p.risque.risqueName) LIKE LOWER(CONCAT('%', :risque, '%'))) " +
            "AND (:codeRisque IS NULL OR p.risque.codeRisque = :codeRisque)")
    List<Production> searchContracts(@Param("keyword") String keyword,
            @Param("risque") String risque,
            @Param("codeRisque") Integer codeRisque);

}
