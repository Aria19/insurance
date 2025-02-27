package com.maghrebia.data_extract.DAO.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maghrebia.data_extract.DAO.Entities.Contacts;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactsRepository extends JpaRepository<Contacts, Long> {

        /*
         * @Query("SELECT c FROM Contacts c " +
         * "WHERE (:searchTerm IS NULL OR " +
         * "LOWER(c.assure) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
         * "LOWER(c.msh) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
         * "LOWER(c.societe) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
         */
        @Query(value = """
                        SELECT c.*
                        FROM contacts c
                        LEFT JOIN production p ON c.id_contact = p.id_contact
                        LEFT JOIN risque r ON p.id_risque = r.id_risque
                        WHERE (c.assure LIKE CONCAT('%', :assure, '%')
                               OR c.msh LIKE CONCAT('%', :msh, '%')
                               OR c.societe LIKE CONCAT('%', :societe, '%')
                               OR r.code_risque LIKE CONCAT('%', :codeRisque, '%')
                               OR p.numero_contrat LIKE CONCAT('%', :numeroContrat, '%'))
                        """, nativeQuery = true)
        List<Contacts> searchContacts(
                        @Param("assure") String assure,
                        @Param("msh") String msh,
                        @Param("societe") String societe,
                        @Param("codeRisque") Integer codeRisque,
                        @Param("numeroContrat") String numeroContrat);

        @Query("SELECT COUNT(c) > 0 FROM Contacts c " +
                        "WHERE c.assure = :assure " +
                        "OR c.telephone = :telephone " +
                        "OR (COALESCE(:email, '') <> '' AND c.email = :email) " +
                        "OR (COALESCE(:cin, '') <> '' AND c.cin = :cin)")
        boolean existsByUniqueFields(@Param("assure") String assure,
                        @Param("email") String email,
                        @Param("telephone") String telephone,
                        @Param("cin") String cin);

        Optional<Contacts> findByAssure(String assure);

        Optional<Contacts> findBySociete(String societe);
}
