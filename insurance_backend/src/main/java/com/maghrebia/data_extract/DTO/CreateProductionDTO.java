package com.maghrebia.data_extract.DTO;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProductionDTO {

    private String nature;	
    private Integer codeRisque;		
    private Date dateEffet;	
    private Date dateEcheance;
    private Integer mois;
    private String dureeContrat;	
    private String modePayement;		
    private Integer nombreCheque;		
    private String numeroCheque;	
    private Date dateDuCheque;
    private float primeNette; 	
    private float prime;		
    private float commission;
    private String remarques;

    private List<CreateBanqueDTO> transactions;

}
