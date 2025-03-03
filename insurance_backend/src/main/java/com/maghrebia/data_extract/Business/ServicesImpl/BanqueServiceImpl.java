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

import com.maghrebia.data_extract.Business.Services.BanqueService;
import com.maghrebia.data_extract.DAO.Entities.Banque;
import com.maghrebia.data_extract.DAO.Entities.Contacts;
import com.maghrebia.data_extract.DAO.Entities.Production;
import com.maghrebia.data_extract.DAO.Entities.Risque;
import com.maghrebia.data_extract.DAO.Repositories.BanqueRepository;
import com.maghrebia.data_extract.DTO.BanqueDTO;
import com.maghrebia.data_extract.Utils.ExcelCellUtil;
import com.maghrebia.data_extract.Utils.ExcelRowUtil;

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

        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            String numeroContrat = ExcelCellUtil.getCellValue(row, 0, String.class);
            String assure = ExcelCellUtil.getCellValue(row, 1, String.class);
            Date date = ExcelCellUtil.getCellValue(row, 2, Date.class);
            Float montant = ExcelCellUtil.getCellValue(row, 3, Float.class);
            String terme = ExcelCellUtil.getCellValue(row, 4, String.class);
            String modePayement = ExcelCellUtil.getCellValue(row, 5, String.class);
            String nt = ExcelCellUtil.getCellValue(row, 6, String.class);
            Integer codeRisque = ExcelCellUtil.getCellValue(row, 7, Integer.class);
            String bvBanque = ExcelCellUtil.getCellValue(row, 8, String.class);
            String bvPortail = ExcelCellUtil.getCellValue(row, 9, String.class);
            String numeroFeuilleDeCaisse = ExcelCellUtil.getCellValue(row, 10, String.class);
            String remarque = ExcelCellUtil.getCellValue(row, 11, String.class);

            if (ExcelRowUtil.isRowEmpty(numeroContrat, assure, date, montant, terme, modePayement,
                    nt, codeRisque, bvBanque, bvPortail, numeroFeuilleDeCaisse, remarque)) {
                continue;
            }

            Optional<Contacts> contactOpt = contactsServiceImpl.findByAssure(assure);
            Optional<Contacts> societeOpt = contactsServiceImpl.findBySociete(assure);
            Optional<Production> productionOpt = productionServiceImpl.findByNumeroContrat(numeroContrat);
            Optional<Risque> risqueOpt = risqueServiceImpl.findBycodeRisque(codeRisque);

            Contacts contact;

            if (contactOpt.isPresent()) {
                contact = contactOpt.get();
            } else if (societeOpt.isPresent()) {
                contact = societeOpt.get();
            } else {
                contact = null;
            }

            if (contact != null && productionOpt.isPresent() && risqueOpt.isPresent()) {
                Production production = productionOpt.get();
                //Risque risque = risqueOpt.get();

                if (production.getContact().getIdContact().equals(contact.getIdContact())) {
                    Banque banque = new Banque();

                    banque.setDate(date);
                    banque.setMontant(montant);
                    banque.setTerme(terme);
                    banque.setModePayement(modePayement);
                    banque.setNt(nt);
                    banque.setBvBanque(bvBanque);
                    banque.setBvPortail(bvPortail);
                    banque.setNumeroFeuilleDeCaisse(numeroFeuilleDeCaisse);
                    banque.setRemarque(remarque);

                    // Link Production to Banque
                    banque.setContract(production);

                    // Link Contact to Banque
                    banque.setContact(contact);

                    // Link Risque to Banque
                    //banque.setRisque(risque);

                    banqueRepository.save(banque);
                } else {
                    logger.info("Skipping row: Client does not match the contract number for " +
                            assure + " and " + numeroContrat);
                }
            } else {
                logger.info("Skipping row: Contract or client not found for " + numeroContrat
                        + " and " + assure);
            }
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

}
