package com.maghrebia.data_extract.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RisqueDTO {

    private Integer codeRisque;
    private String risqueName;
    private Float commission;
}
