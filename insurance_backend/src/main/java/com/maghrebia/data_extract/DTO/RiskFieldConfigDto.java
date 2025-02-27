package com.maghrebia.data_extract.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RiskFieldConfigDto {

    private Long risqueId;
    private String requiredFields; 

}
