package com.maghrebia.data_extract.Business.ServicesImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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

import com.maghrebia.data_extract.Business.Services.BanqueService;
import com.maghrebia.data_extract.DAO.Entities.Banque;
import com.maghrebia.data_extract.DAO.Entities.Contacts;
import com.maghrebia.data_extract.DAO.Entities.Production;
import com.maghrebia.data_extract.DAO.Entities.Risque;
import com.maghrebia.data_extract.DAO.Repositories.BanqueRepository;
import com.maghrebia.data_extract.DTO.BanqueDTO;
import com.maghrebia.data_extract.Mapper.BanqueMapper;
import com.maghrebia.data_extract.Mapper.CreateBanqueMapper;
import com.maghrebia.data_extract.Utils.ExcelCellUtil;
import com.maghrebia.data_extract.Utils.ExcelRowUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BanqueServiceImpl implements BanqueService {

    private final BanqueRepository banqueRepository;
    private final ContactsServiceImpl contactsServiceImpl;
    private final ProductionServiceImpl productionServiceImpl;
    private final RisqueServiceImpl risqueServiceImpl;
    private final Logger logger = LoggerFactory.getLogger(BanqueServiceImpl.class);
    private final CreateBanqueMapper createBanqueMapper;
    private final BanqueMapper banqueMapper;

    public BanqueServiceImpl(BanqueRepository banqueRepository,
            ContactsServiceImpl contactsServiceImpl,
            ProductionServiceImpl productionServiceImpl,
            RisqueServiceImpl risqueServiceImpl,
            CreateBanqueMapper createBanqueMapper, 
            BanqueMapper banqueMapper) {
        this.banqueRepository = banqueRepository;
        this.contactsServiceImpl = contactsServiceImpl;
        this.productionServiceImpl = productionServiceImpl;
        this.risqueServiceImpl = risqueServiceImpl;
        this.createBanqueMapper = createBanqueMapper;
        this.banqueMapper = banqueMapper;
    }

    @Override
    public void importBanques(Sheet sheet) {
        Iterator<Row> rowIterator = sheet.iterator();

        if (rowIterator.hasNext())
            rowIterator.next(); // Skip header

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            BanqueData data = parseRow(row);

            if (data.isEmpty())
                continue;

            Optional<Contacts> contactOpt = contactsServiceImpl.findByAssure(data.assure());
            Optional<Contacts> societeOpt = contactsServiceImpl.findBySociete(data.assure());
            Optional<Production> productionOpt = productionServiceImpl.findByNumeroContrat(data.numeroContrat());
            Optional<Risque> risqueOpt = risqueServiceImpl.findBycodeRisque(data.codeRisque());

            Contacts contact = findOrCreateContact(contactOpt, societeOpt);
            if (contact == null || productionOpt.isEmpty() || risqueOpt.isEmpty()) {
                logger.info("Skipping row: Contract or client not found for " + data.numeroContrat() + " and "
                        + data.assure());
                continue;
            }

            Production production = productionOpt.get();
            if (!production.getContact().getIdContact().equals(contact.getIdContact())) {
                logger.info("Skipping row: Client does not match the contract number for " + data.assure() + " and "
                        + data.numeroContrat());
                continue;
            }

            createBanqueEntry(data, contact, production);
        }
    }

    /** Extracts data from a row */
    private BanqueData parseRow(Row row) {
        return new BanqueData(
                ExcelCellUtil.getCellValue(row, 0, String.class),
                ExcelCellUtil.getCellValue(row, 1, String.class),
                ExcelCellUtil.getCellValue(row, 2, Date.class),
                ExcelCellUtil.getCellValue(row, 3, Float.class),
                ExcelCellUtil.getCellValue(row, 4, String.class),
                ExcelCellUtil.getCellValue(row, 5, String.class),
                ExcelCellUtil.getCellValue(row, 6, String.class),
                ExcelCellUtil.getCellValue(row, 7, Integer.class),
                ExcelCellUtil.getCellValue(row, 8, String.class),
                ExcelCellUtil.getCellValue(row, 9, String.class),
                ExcelCellUtil.getCellValue(row, 10, String.class),
                ExcelCellUtil.getCellValue(row, 11, String.class));
    }

    // Finds or assigns a contact
    private Contacts findOrCreateContact(Optional<Contacts> contactOpt, Optional<Contacts> societeOpt) {
        return contactOpt.orElse(societeOpt.orElse(null));
    }

    private void createBanqueEntry(BanqueData data, Contacts contact, Production production) {
        Banque banque = new Banque();
        banque.setDate(data.date());
        banque.setMontant(data.montant());
        banque.setTerme(data.terme());
        banque.setModePayement(data.modePayement());
        banque.setNt(data.nt());
        banque.setBvBanque(data.bvBanque());
        banque.setBvPortail(data.bvPortail());
        banque.setNumeroFeuilleDeCaisse(data.numeroFeuilleDeCaisse());
        banque.setRemarque(data.remarque());
        banque.setContract(production);
        banque.setContact(contact);

        banqueRepository.save(banque);
    }

    private record BanqueData(
            String numeroContrat, String assure, Date date, Float montant, String terme,
            String modePayement, String nt, Integer codeRisque, String bvBanque,
            String bvPortail, String numeroFeuilleDeCaisse, String remarque) {
        boolean isEmpty() {
            return ExcelRowUtil.isRowEmpty(numeroContrat, assure, date, montant, terme, modePayement,
                    nt, codeRisque, bvBanque, bvPortail, numeroFeuilleDeCaisse, remarque);
        }
    }

    @Override
    public List<BanqueDTO> getAllBanques() {
        List<Banque> banques = banqueRepository.findAll();

        return banques.stream()
                .map(banque -> new BanqueDTO(
                        banque.getContract().getNumeroContrat(),
                        banque.getContact().getAssure(),
                        banque.getDate(),
                        banque.getMontant(),
                        banque.getTerme(),
                        banque.getModePayement(),
                        banque.getNt(),
                        banque.getContract().getRisque().getCodeRisque(),
                        banque.getBvBanque(),
                        banque.getBvPortail(),
                        banque.getNumeroFeuilleDeCaisse(),
                        banque.getRemarque()))
                .toList();
    }

    @Transactional
    public void deleteBanqueEntry(Long idTransaction) {
        Optional<Banque> banqueOpt = banqueRepository.findById(idTransaction);

        if (banqueOpt.isEmpty()) {
            throw new EntityNotFoundException("Banque transaction with ID " + idTransaction + " not found.");
        }

        Banque banque = banqueOpt.get();

        banqueRepository.delete(banque);
    }

    public BanqueDTO updateBanque(Long idBanque, BanqueDTO banqueDTO) {
        return banqueRepository.findById(idBanque)
                .map(existingBanque -> {
                    existingBanque.setDate(banqueDTO.getDate());
                    existingBanque.setMontant(banqueDTO.getMontant());
                    existingBanque.setTerme(banqueDTO.getTerme());
                    existingBanque.setModePayement(banqueDTO.getModePayement());
                    existingBanque.setNt(banqueDTO.getNt());
                    existingBanque.setBvBanque(banqueDTO.getBvBanque());
                    existingBanque.setBvPortail(banqueDTO.getBvPortail());
                    existingBanque.setNumeroFeuilleDeCaisse(banqueDTO.getNumeroFeuilleDeCaisse());
                    existingBanque.setRemarque(banqueDTO.getRemarque());

                    Banque updatedBanque = banqueRepository.save(existingBanque);
                    return new BanqueDTO(
                            updatedBanque.getContract().getNumeroContrat(),
                            updatedBanque.getContact().getAssure(),
                            updatedBanque.getDate(),
                            updatedBanque.getMontant(),
                            updatedBanque.getTerme(),
                            updatedBanque.getModePayement(),
                            updatedBanque.getNt(),
                            updatedBanque.getContract().getRisque().getCodeRisque(),
                            updatedBanque.getBvBanque(),
                            updatedBanque.getBvPortail(),
                            updatedBanque.getNumeroFeuilleDeCaisse(),
                            updatedBanque.getRemarque());
                })
                .orElseThrow(() -> new RuntimeException("Banque with id " + idBanque + " not found"));
    }

    public ResponseEntity<ByteArrayResource> exportBanquesToExcel() {
        // Fetch all banque transactions
        List<Banque> banques = banqueRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Banque Transactions");

            // Create a header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Date");
            header.createCell(1).setCellValue("Montant");
            header.createCell(2).setCellValue("Mode Payement");

            // Fill data rows
            int rowNum = 1;
            for (Banque banque : banques) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(banque.getDate().toString());
                row.createCell(1).setCellValue(banque.getMontant());
                row.createCell(2).setCellValue(banque.getModePayement()); // Fixed index
            }

            // Write workbook to byte array
            workbook.write(outputStream);
            byte[] byteArray = outputStream.toByteArray();

            ByteArrayResource resource = new ByteArrayResource(byteArray);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=banques_transactions.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public List<BanqueDTO> getTransactionsByProductionId(Long productionId) {
        List<Banque> transactions = banqueRepository.findByContract_IdProduction(productionId);
        return transactions.stream()
                .map(banqueMapper::toDto)
                .toList();
    }
}
