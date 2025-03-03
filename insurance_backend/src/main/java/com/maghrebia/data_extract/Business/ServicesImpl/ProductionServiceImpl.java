package com.maghrebia.data_extract.Business.ServicesImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maghrebia.data_extract.Business.Services.ProductionService;
import com.maghrebia.data_extract.DAO.Entities.Banque;
import com.maghrebia.data_extract.DAO.Entities.Contacts;
import com.maghrebia.data_extract.DAO.Entities.Production;
import com.maghrebia.data_extract.DAO.Entities.Risque;
import com.maghrebia.data_extract.DAO.Repositories.BanqueRepository;
import com.maghrebia.data_extract.DAO.Repositories.ProductionRepository;
import com.maghrebia.data_extract.DTO.ProductionDTO;
import com.maghrebia.data_extract.Mapper.ProductionMapper;
import com.maghrebia.data_extract.Utils.ExcelCellUtil;
import com.maghrebia.data_extract.Utils.ExcelRowUtil;
import com.maghrebia.data_extract.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

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

    /* @Transactional
    public void deleteProduction(Long idProduction) {
        Production production = productionRepository.findById(idProduction)
                .orElseThrow(() -> new ResourceNotFoundException("Production not found"));

        // Manually handle orphan removal if needed
        for (Banque banque : production.getTransactions()) {
            banque.setContract(null); // Remove the reference to Production
            banqueRepository.save(banque);
        }

        productionRepository.delete(production); // Delete the Production entity
    } */

}
