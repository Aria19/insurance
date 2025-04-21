package com.maghrebia.data_extract.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactsDTO {

    private Long idContact;
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

    public ContactsDTO(String assure, String societe, String telephone, String email, String msh,
            String motDePasse, String cin, String carteSejour, String passeport, String matriculeFiscale) {
        this.assure = assure;
        this.societe = societe;
        this.telephone = telephone;
        this.email = email;
        this.msh = msh;
        this.motDePasse = motDePasse;
        this.cin = cin;
        this.carteSejour = carteSejour;
        this.passeport = passeport;
        this.matriculeFiscale = matriculeFiscale;
    }

}
