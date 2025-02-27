package com.maghrebia.data_extract.Web.RestControllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maghrebia.data_extract.Business.ServicesImpl.ProductionServiceImpl;
import com.maghrebia.data_extract.DTO.ProductionDTO;

@RestController
@RequestMapping("/api/productions")
public class ProductionController {

    private final ProductionServiceImpl productionServiceImpl;

    public ProductionController(ProductionServiceImpl productionServiceImpl) {
        this.productionServiceImpl = productionServiceImpl;
    }

    @GetMapping("/view")
    public ResponseEntity<List<ProductionDTO>> getAllProductions(){
        return ResponseEntity.ok(productionServiceImpl.getAllProductions());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductionDTO>> searchContracts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "risque", required = false) String risque,
            @RequestParam(value = "codeRisque", required = false) Integer codeRisque) {
        
        List<ProductionDTO> contracts = productionServiceImpl.searchContracts(keyword, risque, codeRisque);
        
        if (contracts.isEmpty()) {
            return ResponseEntity.noContent().build();  // No contracts found
        }
        
        return ResponseEntity.ok(contracts);  // Return the list of contracts
    }
}
