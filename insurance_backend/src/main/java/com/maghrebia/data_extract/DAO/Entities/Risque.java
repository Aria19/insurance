package com.maghrebia.data_extract.DAO.Entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "risque")
public class Risque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRisque;

    private Integer codeRisque;
    private String risqueName;
    private Float commission;

    @OneToMany(mappedBy = "risque", cascade = CascadeType.ALL, orphanRemoval =true)
    private Set<Production> contracts;

    @OneToMany(mappedBy = "risque", cascade = CascadeType.ALL, orphanRemoval =true)
    private Set<Banque> transactions;
}
