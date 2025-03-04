package com.maghrebia.data_extract.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateContactDto {

    private String assure;
    private String societe;
    private String telephone;
    private String email;
    private String msh;
    private String motDePasse;
    private String cin;
    private String carteSejour;
    private String passeport;
    private String matriculeFiscale;

    private List<CreateProductionDTO> contracts;
    private List<CreateBanqueDTO> transactions;
}
