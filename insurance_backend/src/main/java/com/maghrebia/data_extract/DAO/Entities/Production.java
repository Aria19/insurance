package com.maghrebia.data_extract.DAO.Entities;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "production")
public class Production {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduction;

    @Column(unique = true, nullable = false)
    private String numeroContrat;

    private String nature;
    // private String risque;
    // private Integer codeRisque;

    @Temporal(TemporalType.DATE)
    private Date dateEffet;

    @Temporal(TemporalType.DATE)
    private Date dateEcheance;

    private Integer mois;

    private String dureeContrat;
    private String modePayement;
    private Integer nombreCheque;
    private String numeroCheque;

    @Temporal(TemporalType.DATE)
    private Date dateDuCheque;
    
    private float primeNette;
    private float prime;
    private float commission;
    private String remarques;
    // private String assuré;

    @ManyToOne
    @JoinColumn(name = "id_contact", referencedColumnName = "idContact", nullable = false)
    private Contacts contact;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Banque> transactions;

    @ManyToOne
    @JoinColumn(name = "id_risque", referencedColumnName = "idRisque", nullable = false)
    private Risque risque;
}
