package com.maghrebia.data_extract.DAO.Entities;

import java.util.List;

import com.maghrebia.data_extract.DAO.Entities.OptionControl.RoleOption;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<RoleOption> roles;
}
