package com.maghrebia.data_extract.Business.ServicesImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maghrebia.data_extract.Business.Services.ProductionService;
import com.maghrebia.data_extract.DAO.Entities.Contacts;
import com.maghrebia.data_extract.DAO.Entities.Production;
import com.maghrebia.data_extract.DAO.Entities.Risque;
import com.maghrebia.data_extract.DAO.Repositories.BanqueRepository;
import com.maghrebia.data_extract.DAO.Repositories.ProductionRepository;
import com.maghrebia.data_extract.DTO.ProductionDTO;
import com.maghrebia.data_extract.Mapper.ProductionMapper;
import com.maghrebia.data_extract.Utils.ExcelCellUtil;
import com.maghrebia.data_extract.Utils.ExcelRowUtil;

@Service
public class ProductionServiceImpl implements ProductionService {

    final ProductionRepository productionRepository;
    final BanqueRepository banqueRepository;
    final ProductionMapper productionMapper;
    final ContactsServiceImpl contactsServiceImpl;
    final RisqueServiceImpl risqueServiceImpl;
    private final Logger logger = LoggerFactory.getLogger(ProductionServiceImpl.class);

    public ProductionServiceImpl(ProductionRepository productionRepository,
            BanqueRepository banqueRepository,
            ProductionMapper productionMapper,
            ContactsServiceImpl contactsServiceImpl,
            RisqueServiceImpl risqueServiceImpl) {
        this.productionRepository = productionRepository;
        this.banqueRepository = banqueRepository;
        this.productionMapper = productionMapper;
        this.contactsServiceImpl = contactsServiceImpl;
        this.risqueServiceImpl = risqueServiceImpl;
    }

    @Override
    public void importProduction(Sheet sheet) {
        Iterator<Row> rowIterator = sheet.iterator();

        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {

            Row row = rowIterator.next();

            String numeroContrat = ExcelCellUtil.getCellValue(row, 0, String.class);
            String assure = ExcelCellUtil.getCellValue(row, 1, String.class);
            String nature = ExcelCellUtil.getCellValue(row, 2, String.class);
            String risqueName = ExcelCellUtil.getCellValue(row, 3, String.class);
            Integer codeRisque = ExcelCellUtil.getCellValue(row, 4, Integer.class);
            Date dateEffet = ExcelCellUtil.getCellValue(row, 5, Date.class);
            Date dateEcheance = ExcelCellUtil.getCellValue(row, 6, Date.class);
            Integer mois = ExcelCellUtil.getCellValue(row, 7, Integer.class);
            String dureeContrat = ExcelCellUtil.getCellValue(row, 8, String.class);
            String modePayement = ExcelCellUtil.getCellValue(row, 9, String.class);
            Integer nombreCheque = ExcelCellUtil.getCellValue(row, 10, Integer.class);
            String numeroCheque = ExcelCellUtil.getCellValue(row, 11, String.class);
            Date dateDuCheque = ExcelCellUtil.getCellValue(row, 12, Date.class);
            Float primeNette = ExcelCellUtil.getCellValue(row, 13, Float.class);
            Float prime = ExcelCellUtil.getCellValue(row, 14, Float.class);
            Float commission = ExcelCellUtil.getCellValue(row, 15, Float.class);
            String remarques = ExcelCellUtil.getCellValue(row, 16, String.class);

            if (ExcelRowUtil.isRowEmpty(numeroContrat, nature, risqueName, codeRisque, dateEffet, dateEcheance,
                    dureeContrat, modePayement, nombreCheque, numeroCheque, dateDuCheque,
                    primeNette, prime, commission, remarques)) {
                continue;
            }

            /*
             * if (numeroContrat == null || numeroContrat.trim().isEmpty()) {
             * numeroContrat = UUID.randomUUID().toString().replaceAll("[^A-Za-z1-9]",
             * "").substring(0, 10);
             * }
             */

            if (productionRepository.existsByNumeroContrat(numeroContrat)) {
                continue;
            }

            Optional<Contacts> contactOpt = contactsServiceImpl.findByAssure(assure);
            Optional<Contacts> societeOpt = contactsServiceImpl.findBySociete(assure);
            Optional<Risque> risqueOpt = risqueServiceImpl.findBycodeRisque(codeRisque);

            Contacts contact = contactOpt.orElse(societeOpt.orElse(null));

            if (contact != null && risqueOpt.isPresent()) {

                Risque risque = risqueOpt.get();

                // Create Production entity
                Production production = new Production();
                production.setNumeroContrat(numeroContrat);
                production.setNature(nature);
                production.setDateEffet(dateEffet);
                production.setDateEcheance(dateEcheance);
                production.setMois(mois);
                production.setDureeContrat(dureeContrat);
                production.setModePayement(modePayement);
                production.setNombreCheque(nombreCheque);
                production.setNumeroCheque(numeroCheque);
                production.setDateDuCheque(dateDuCheque);
                production.setPrimeNette(primeNette);
                production.setPrime(prime);
                production.setCommission(commission);
                production.setRemarques(remarques);

                // Link Contact to Production
                production.setContact(contact);

                // Link Risque to Production
                production.setRisque(risque);

                // Save the production entity
                productionRepository.save(production);
            } else {
                logger.info("Contact not found for: " + assure);
            }
        }
    }

    @Override
    public Optional<Production> findByNumeroContrat(String numeroContrat) {
        return productionRepository.findByNumeroContrat(numeroContrat);
    }

    @Override
    public List<ProductionDTO> getAllProductions() {
        List<Production> productions = productionRepository.findAll();
        return productions.stream().map(production -> new ProductionDTO(
                production.getNumeroContrat(),
                production.getContact().getAssure(),
                production.getNature(),
                production.getRisque().getRisqueName(),
                production.getRisque().getCodeRisque(),
                production.getDateEffet(),
                production.getDateEcheance(),
                production.getMois(),
                production.getDureeContrat(),
                production.getModePayement(),
                production.getNombreCheque(),
                production.getNumeroCheque(),
                production.getDateDuCheque(),
                production.getPrimeNette(),
                production.getPrime(),
                production.getCommission(),
                production.getRemarques()))
                .toList();
    }

    @Override
    public List<ProductionDTO> searchContracts(String keyword, String risk, Integer code) {
        List<ProductionDTO> contracts = new ArrayList<>();

        // Fetch CarInsurance entities, map to CarInsuranceDTO
        List<ProductionDTO> productionDTOs = productionRepository
                .searchContracts(keyword, risk, code)
                .stream()
                .map(production -> new ProductionDTO(
                        production.getNumeroContrat(),
                        production.getContact().getAssure(),
                        production.getNature(),
                        production.getRisque().getRisqueName(),
                        production.getRisque().getCodeRisque(),
                        production.getDateEffet(),
                        production.getDateEcheance(),
                        production.getMois(),
                        production.getDureeContrat(),
                        production.getModePayement(),
                        production.getNombreCheque(),
                        production.getNumeroCheque(),
                        production.getDateDuCheque(),
                        production.getPrimeNette(),
                        production.getPrime(),
                        production.getCommission(),
                        production.getRemarques()))
                .toList();

        contracts.addAll(productionDTOs);

        return contracts;
    }

    @Override
    @Transactional
    public String deleteProduction(Long idProduction) {
        logger.info("Production repository: " + productionRepository);
        try {
            if (!productionRepository.existsById(idProduction)) {
                throw new RuntimeException("Contract with id " + idProduction +
                        " not found");
            }
            banqueRepository.deleteByContractId(idProduction);
            productionRepository.deleteById(idProduction);
            return "Contract deleted successfully";

        } catch (Exception e) {
            logger.error("Error while deleting contract with id " + idProduction, e);
            throw new RuntimeException("Error while deleting contract with id " +
                    idProduction + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void updateProduction(Long idProduction, ProductionDTO productionDTO) {
        productionRepository.findById(idProduction)
                .ifPresent(currentContract -> {
                    currentContract.setNature(productionDTO.getNature());
                    currentContract.setDateEffet(productionDTO.getDateEffet());
                    currentContract.setDateEcheance(productionDTO.getDateEcheance());
                    currentContract.setMois(productionDTO.getMois());
                    currentContract.setDureeContrat(productionDTO.getDureeContrat());
                    currentContract.setModePayement(productionDTO.getModePayement());
                    currentContract.setNombreCheque(productionDTO.getNombreCheque());
                    currentContract.setNumeroCheque(productionDTO.getNumeroCheque());
                    currentContract.setDateDuCheque(productionDTO.getDateDuCheque());
                    currentContract.setPrimeNette(productionDTO.getPrimeNette());
                    currentContract.setPrime(productionDTO.getPrime());
                    currentContract.setCommission(productionDTO.getCommission());
                    currentContract.setRemarques(productionDTO.getRemarques());

                    productionRepository.save(currentContract);
                });
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportProuctionToExcel() {
        List<Production> contracts = productionRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Contracts");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Contrat");
            header.createCell(1).setCellValue("Assure");
            header.createCell(2).setCellValue("Nature");
            header.createCell(3).setCellValue("Risque");
            header.createCell(4).setCellValue("Code");
            header.createCell(5).setCellValue("Effet");
            header.createCell(6).setCellValue("Echeance");
            header.createCell(7).setCellValue("Mois");
            header.createCell(8).setCellValue("Duree");
            header.createCell(9).setCellValue("Mode P");
            header.createCell(10).setCellValue("NBR");
            header.createCell(11).setCellValue("N Cheque");
            header.createCell(12).setCellValue("Date Du Cheque");
            header.createCell(13).setCellValue("Prime Nette");
            header.createCell(14).setCellValue("Prime");
            header.createCell(15).setCellValue("Commission");
            header.createCell(16).setCellValue("Remarques");

            CellStyle numberStyle = workbook.createCellStyle();
            
            int rowNum = 1;
            for (Production contract : contracts) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(contract.getNumeroContrat());
                row.createCell(1).setCellValue(contract.getContact().getAssure());
                row.createCell(2).setCellValue(contract.getNature());
                row.createCell(3).setCellValue(contract.getRisque().getRisqueName());
                row.createCell(4).setCellValue(contract.getRisque().getCodeRisque());
                row.createCell(5).setCellValue(contract.getDateEffet().toString());
                row.createCell(6).setCellValue(contract.getDateEcheance().toString());
                row.createCell(7).setCellValue(Optional.ofNullable(contract.getMois()).orElse(0));
                row.createCell(8).setCellValue(contract.getDureeContrat());
                row.createCell(9).setCellValue(contract.getModePayement());
                row.createCell(10).setCellValue(Optional.ofNullable(contract.getNombreCheque()).orElse(0));
                row.createCell(11).setCellValue(contract.getNumeroCheque());
                String dateDuCheque = Optional.ofNullable(contract.getDateDuCheque())
                        .map(Object::toString)
                        .orElse("N/A");
                row.createCell(12).setCellValue(dateDuCheque);

                Cell primeNetteCell = row.createCell(13);
                primeNetteCell.setCellValue(contract.getPrimeNette());
                primeNetteCell.setCellStyle(numberStyle);

                Cell primeCell = row.createCell(14);
                primeCell.setCellValue(contract.getPrime());
                primeCell.setCellStyle(numberStyle);

                Cell commissionCell = row.createCell(15);
                commissionCell.setCellValue(contract.getCommission());
                commissionCell.setCellStyle(numberStyle);

                row.createCell(16).setCellValue(contract.getRemarques());
            }

            workbook.write(outputStream);

            byte[] byteArray = outputStream.toByteArray();

            ByteArrayResource resource = new ByteArrayResource(byteArray);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contracts.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
