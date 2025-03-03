package com.maghrebia.data_extract.Business.ServicesImpl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maghrebia.data_extract.Business.Services.BanqueService;
import com.maghrebia.data_extract.DAO.Entities.Banque;
import com.maghrebia.data_extract.DAO.Entities.Contacts;
import com.maghrebia.data_extract.DAO.Entities.Production;
import com.maghrebia.data_extract.DAO.Entities.Risque;
import com.maghrebia.data_extract.DAO.Repositories.BanqueRepository;
import com.maghrebia.data_extract.DTO.BanqueDTO;
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

    public BanqueServiceImpl(BanqueRepository banqueRepository,
            ContactsServiceImpl contactsServiceImpl,
            ProductionServiceImpl productionServiceImpl,
            RisqueServiceImpl risqueServiceImpl) {
        this.banqueRepository = banqueRepository;
        this.contactsServiceImpl = contactsServiceImpl;
        this.productionServiceImpl = productionServiceImpl;
        this.risqueServiceImpl = risqueServiceImpl;
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
                ExcelCellUtil.getCellValue(row, 11, String.class) 
        );
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
    public void deleteBanqueEntry(Long idTransaction){
        Optional<Banque> banqueOpt = banqueRepository.findById(idTransaction);

        if(banqueOpt.isEmpty()){
            throw new EntityNotFoundException("Banque transaction with ID " + idTransaction + " not found.");
        }

        Banque banque = banqueOpt.get();
        
        banqueRepository.delete(banque);
    }

}
