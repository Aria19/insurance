package com.maghrebia.data_extract.DTO;

import com.maghrebia.data_extract.DAO.Entities.Risque;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RisqueDTO {

    private Long idRisque;
    private Integer codeRisque;
    private String risqueName;
    private Float commission;

    public RisqueDTO(Integer codeRisque, String risqueName, Float commission) {
        this.codeRisque = codeRisque;
        this.risqueName = risqueName;
        this.commission = commission;
    }

    public RisqueDTO(Risque risque) {
        this.idRisque = risque.getIdRisque();
        this.codeRisque = risque.getCodeRisque();
        this.risqueName = risque.getRisqueName();
        this.commission = risque.getCommission();
    }
}
