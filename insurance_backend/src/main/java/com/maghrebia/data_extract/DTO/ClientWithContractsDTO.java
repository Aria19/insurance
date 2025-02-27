package com.maghrebia.data_extract.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientWithContractsDTO {

    private String assure;
    private String societe;
    private String telephone;
    private String email;
    private List<ProductionDTO>  contracts;
    
}
