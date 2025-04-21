package com.maghrebia.data_extract.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductionWithTransactionsDTO {
    private ProductionDTO production;
    private List<BanqueDTO> transactions;
}

