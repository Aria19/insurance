package com.maghrebia.data_extract.DAO.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.maghrebia.data_extract.DAO.Entities.Production;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {

        boolean existsByNumeroContrat(String numeroContrat);

        boolean existsById(Long idProduction);

        Optional<Production> findByNumeroContrat(String numeroContrat);

        @Query("SELECT p FROM Production p WHERE " +
                        "(:keyword IS NULL OR LOWER(p.contact.assure) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.contact.msh) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                        "AND (:risque IS NULL OR LOWER(p.risque.risqueName) LIKE LOWER(CONCAT('%', :risque, '%'))) " +
                        "AND (:codeRisque IS NULL OR p.risque.codeRisque = :codeRisque)" +
                        "AND (:dateEffet IS NULL OR FUNCTION('YEAR', p.dateEffet) = :dateEffet)")
        List<Production> searchContracts(@Param("keyword") String keyword,
                        @Param("risque") String risque,
                        @Param("codeRisque") Integer codeRisque,
                        @Param("dateEffet") Integer dateEffet);

        @Transactional
        @Modifying
        @Query("DELETE FROM Production p WHERE p.contact.idContact = :idContact")
        void deleteByContactId(@Param("idContact") Long idContact);
}
