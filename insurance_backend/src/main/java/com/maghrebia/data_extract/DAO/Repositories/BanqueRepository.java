package com.maghrebia.data_extract.DAO.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.maghrebia.data_extract.DAO.Entities.Banque;

@Repository
public interface BanqueRepository extends JpaRepository<Banque, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Banque b WHERE b.contract.idProduction = :idProduction")
    void deleteByContractId(@Param("idProduction") Long idProduction);

    @Transactional
        @Modifying
        @Query("DELETE FROM Banque b WHERE b.contact.idContact = :idContact")
        void deleteByContactId(@Param("idContact") Long idContact);
}
