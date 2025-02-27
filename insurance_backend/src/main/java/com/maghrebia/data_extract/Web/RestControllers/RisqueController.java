package com.maghrebia.data_extract.Web.RestControllers;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.maghrebia.data_extract.Business.ServicesImpl.RisqueServiceImpl;

@RestController
@RequestMapping("/api/risques")
public class RisqueController {

    private final RisqueServiceImpl risqueServiceImpl;


    public RisqueController(RisqueServiceImpl risqueServiceImpl) {
        this.risqueServiceImpl = risqueServiceImpl;
    }


    @PostMapping(value = "/import", consumes = "multipart/form-data")
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
            risqueServiceImpl.importRisques(workbook.getSheet("Risque"));
            return ResponseEntity.ok("Data imported successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to import data. Make sure the Excel file is not corrupted.");
        }
    }
}
