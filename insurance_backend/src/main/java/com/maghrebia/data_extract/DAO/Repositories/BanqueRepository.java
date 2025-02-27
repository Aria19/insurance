package com.maghrebia.data_extract.DAO.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maghrebia.data_extract.DAO.Entities.Banque;

@Repository
public interface BanqueRepository extends JpaRepository<Banque, Long> {

}
