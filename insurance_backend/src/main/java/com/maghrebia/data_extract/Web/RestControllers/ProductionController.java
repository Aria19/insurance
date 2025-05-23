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

import com.maghrebia.data_extract.Business.Services.ProductionService;
import com.maghrebia.data_extract.DTO.CreateProductionDTO;
import com.maghrebia.data_extract.DTO.ProductionDTO;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/productions")
public class ProductionController {

    private final ProductionService productionService;

    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    @GetMapping("/view")
    public ResponseEntity<List<ProductionDTO>> getAllProductions() {
        return ResponseEntity.ok(productionService.getAllProductions());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductionDTO>> searchContracts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "risque", required = false) String risque,
            @RequestParam(value = "codeRisque", required = false) Integer codeRisque,
            @RequestParam(value = "dateEffet", required = false) Integer dateEffet) {

        List<ProductionDTO> contracts = productionService.searchContracts(keyword, risque,
                codeRisque, dateEffet);

        if (contracts.isEmpty()) {
            return ResponseEntity.noContent().build(); // No contracts found
        }

        return ResponseEntity.ok(contracts); // Return the list of contracts
    }

    @PutMapping("update/{idProduction}")
    public ResponseEntity<String> updateProduction(@PathVariable Long idProduction,
            @RequestBody ProductionDTO productionDTO) {
        productionService.updateProduction(idProduction, productionDTO);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{idProduction}")
    public ResponseEntity<Void> deleteProduction(@PathVariable Long idProduction) {
        try {
            productionService.deleteProduction(idProduction);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/export")
    public ResponseEntity<?> exportProuctionToExcel(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String risk,
            @RequestParam(required = false) Integer code,
            @RequestParam(required = false) Integer dateEffet) {
        return productionService.exportProuctionToExcel(keyword, risk, code, dateEffet);
    }

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<List<ProductionDTO>> getProductionsByContactId(@PathVariable Long contactId) {
        List<ProductionDTO> productions = productionService.getProductionsByContactId(contactId);
        return ResponseEntity.ok(productions);
    }

    @PostMapping("/add/{idContact}")
    public void saveContractWithTransaction(@PathVariable Long idContact,
            @RequestBody CreateProductionDTO contractDto) {
        productionService.saveContractWithTransaction(idContact, contractDto);
    }
}
