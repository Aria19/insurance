package com.maghrebia.data_extract.Web.RestControllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maghrebia.data_extract.Business.Services.BanqueService;
import com.maghrebia.data_extract.Business.ServicesImpl.BanqueServiceImpl;
import com.maghrebia.data_extract.DTO.BanqueDTO;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/Banques")
public class BanqueController {

    private final BanqueService banqueService;

    public BanqueController(BanqueServiceImpl banqueService){
        this.banqueService = banqueService;
    }

    @GetMapping("/view")
    public ResponseEntity<List<BanqueDTO>> getAllBanques(){
        return ResponseEntity.ok(banqueService.getAllBanques());
    }

    @PutMapping("update/{idBanque}")
    public ResponseEntity<BanqueDTO> updateBanque(@PathVariable Long idBanque, @RequestBody BanqueDTO banqueDTO) {
        BanqueDTO updatedBanque = banqueService.updateBanque(idBanque, banqueDTO);
        return ResponseEntity.ok(updatedBanque);
    }

    @DeleteMapping("/delete/{idTransaction}")
    public ResponseEntity<String> deleteBanqueEntry(@PathVariable Long idTransaction){
        try {
            banqueService.deleteBanqueEntry(idTransaction);
            return ResponseEntity.ok("Banque transaction with ID " + idTransaction + " has been deleted.");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } 
    }

    @GetMapping("/export")
    public ResponseEntity<?> exportBanquesToExcel() {
        return banqueService.exportBanquesToExcel();
    }

    @GetMapping("/production/{productionId}")
    public ResponseEntity<List<BanqueDTO>> getTransactionsByProductionId(@PathVariable Long productionId) {
        List<BanqueDTO> transactions = banqueService.getTransactionsByProductionId(productionId);
        return ResponseEntity.ok(transactions);
    }
}
