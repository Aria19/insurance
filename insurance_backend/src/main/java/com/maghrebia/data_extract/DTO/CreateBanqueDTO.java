package com.maghrebia.data_extract.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateBanqueDTO {

    private String contactName;
    private Date date;
    private Float montant;
    private String terme;	
    private String modePayement;
    private String nt;	
    private String bvBanque;	
    private String bvPortail;	
    private String remarque;

}
