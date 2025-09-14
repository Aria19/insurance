package com.maghrebia.data_extract.Web.RestControllers;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.maghrebia.data_extract.Business.Services.RisqueService;
import com.maghrebia.data_extract.Business.ServicesImpl.RisqueServiceImpl;
import com.maghrebia.data_extract.DTO.RisqueDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/risques")
public class RisqueController {

    private final RisqueService risqueService;

    public RisqueController(RisqueServiceImpl risqueService) {
        this.risqueService = risqueService;
    }

    /* @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<String> importDataFromExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !(fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))) {
            return ResponseEntity.badRequest().body("Invalid file type. Please upload an Excel file (.xls or .xlsx).");
        }

        String mimeType = file.getContentType();
        if (mimeType == null ||
                !(mimeType.equals("application/vnd.ms-excel") ||
                        mimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
            return ResponseEntity.badRequest().body("Invalid file format. Please upload a valid Excel file.");
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            risqueService.importRisques(workbook.getSheet("Risque"));
            return ResponseEntity.ok("Data imported successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to import data. Make sure the Excel file is not corrupted.");
        }
    } */

    @GetMapping("/view")
    public List<RisqueDTO> getAllRisques() {
        return risqueService.getAllRisques();
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addRisque(@RequestBody @Valid RisqueDTO risqueDTO) {
        risqueService.addRisque(risqueDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{risqueId}")
    public ResponseEntity<Void> updateRisque(@PathVariable Long risqueId, @RequestBody @Valid RisqueDTO risqueDTO) {
        risqueService.upddateRisque(risqueId, risqueDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/view/{risqueId}")
    public RisqueDTO getRisqueById(@PathVariable Long risqueId) {
        return risqueService.getRisqueByID(risqueId);
    }

    @DeleteMapping("/{risqueId}")
    public ResponseEntity<Void> deleteRisque(@PathVariable Long risqueId) {
        risqueService.deleteRisk(risqueId);
        return ResponseEntity.ok().build();
    }
}
