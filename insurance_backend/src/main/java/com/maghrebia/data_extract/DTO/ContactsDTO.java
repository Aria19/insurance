package com.maghrebia.data_extract.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactsDTO {

    private String assure;
    private String societe;
    private String telephone;
    private String email;
    private String msh;
    private String motDePasse;
    private String cin;
    private String carteSejour;
}
