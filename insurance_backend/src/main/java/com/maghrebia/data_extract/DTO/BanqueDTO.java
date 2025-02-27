package com.maghrebia.data_extract.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BanqueDTO {

    private String numeroContrat;
    private String contactName;
    private Date date;
    private Float montant;
    private String terme;
    private String modePayement;	
    private String nt;	
    private Integer codeRisque;
    private String bvBanque;	
    private String bvPortail;	
    private String numeroFeuilleDeCaisse;
    private String remarque;
}
