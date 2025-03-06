package com.maghrebia.data_extract.Web.RestControllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maghrebia.data_extract.Business.ServicesImpl.ProductionServiceImpl;
import com.maghrebia.data_extract.DTO.ProductionDTO;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/productions")
public class ProductionController {

    private final ProductionServiceImpl productionServiceImpl;

    public ProductionController(ProductionServiceImpl productionServiceImpl) {
        this.productionServiceImpl = productionServiceImpl;
    }

    @GetMapping("/view")
    public ResponseEntity<List<ProductionDTO>> getAllProductions() {
        return ResponseEntity.ok(productionServiceImpl.getAllProductions());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductionDTO>> searchContracts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "risque", required = false) String risque,
            @RequestParam(value = "codeRisque", required = false) Integer codeRisque,
            @RequestParam(value = "dateEffet", required = false) Integer dateEffet) {

        List<ProductionDTO> contracts = productionServiceImpl.searchContracts(keyword, risque, 
        codeRisque, dateEffet);

        if (contracts.isEmpty()) {
            return ResponseEntity.noContent().build(); // No contracts found
        }

        return ResponseEntity.ok(contracts); // Return the list of contracts
    }

    @PutMapping("update/{idProduction}")
    public ResponseEntity<String> updateProduction(@PathVariable Long idProduction,
            @RequestBody ProductionDTO productionDTO) {
        productionServiceImpl.updateProduction(idProduction, productionDTO);
        return ResponseEntity.ok("Banque updated successfully");
    }

    @DeleteMapping("/delete/{idProduction}")
    public ResponseEntity<String> deleteProduction(@PathVariable Long idProduction) {
        try {
            productionServiceImpl.deleteProduction(idProduction);
            return ResponseEntity.ok("Production with Id " + idProduction + "has been deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/export")
    public ResponseEntity<?> exportProuctionToExcel(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String risk,
            @RequestParam(required = false) Integer code,
            @RequestParam(required = false) Integer dateEffet) {
        return productionServiceImpl.exportProuctionToExcel(keyword, risk, code, dateEffet);
    }

}
