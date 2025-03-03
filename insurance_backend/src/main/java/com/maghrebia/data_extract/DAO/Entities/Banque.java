package com.maghrebia.data_extract.DAO.Entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "banque")
public class Banque {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaction;

    @Temporal(TemporalType.DATE)
    private Date date;
    
    private Float montant;
    private String terme;
    private String modePayement;	
    private String nt;	
    private String bvBanque;	
    private String bvPortail;	
    private String numeroFeuilleDeCaisse;
    private String remarque;
    //private String numéroContrat;
    //private String assure;

    @ManyToOne
    @JoinColumn(name = "id_contact", referencedColumnName = "idContact", nullable = false)
    private Contacts contact;

    @ManyToOne
    @JoinColumn(name = "id_production", referencedColumnName = "idProduction", nullable = false)
    private Production contract;

}
