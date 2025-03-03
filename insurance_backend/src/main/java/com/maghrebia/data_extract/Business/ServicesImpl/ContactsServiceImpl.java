package com.maghrebia.data_extract.Business.ServicesImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.maghrebia.data_extract.Business.Services.ContactsService;
import com.maghrebia.data_extract.DAO.Entities.Banque;
import com.maghrebia.data_extract.DAO.Entities.Contacts;
import com.maghrebia.data_extract.DAO.Entities.Production;
import com.maghrebia.data_extract.DAO.Entities.Risque;
import com.maghrebia.data_extract.DAO.Repositories.BanqueRepository;
import com.maghrebia.data_extract.DAO.Repositories.ContactsRepository;
import com.maghrebia.data_extract.DAO.Repositories.ProductionRepository;
import com.maghrebia.data_extract.DTO.ContactsDTO;
import com.maghrebia.data_extract.DTO.CreateContactDto;
import com.maghrebia.data_extract.Mapper.ContactsMapper;
import com.maghrebia.data_extract.Utils.ContractNumberUtil;
import com.maghrebia.data_extract.Utils.ExcelCellUtil;
import com.maghrebia.data_extract.Utils.ExcelRowUtil;

@Service
public class ContactsServiceImpl implements ContactsService {

    private final ContactsRepository contactsRepository;
    private final ContactsMapper contactsMapper;
    private final Logger logger = LoggerFactory.getLogger(ContactsServiceImpl.class);
    private final ProductionRepository productionRepository;
    private final BanqueRepository banqueRepository;
    private final RisqueServiceImpl risqueServiceImpl;

    public ContactsServiceImpl(ContactsRepository contactsRepository,
            ContactsMapper contactsMapper,
            ProductionRepository productionRepository,
            BanqueRepository banqueRepository,
            RisqueServiceImpl risqueServiceImpl) {
        this.contactsRepository = contactsRepository;
        this.contactsMapper = contactsMapper;
        this.productionRepository = productionRepository;
        this.banqueRepository = banqueRepository;
        this.risqueServiceImpl = risqueServiceImpl;
    }

    @Override
    public void importContacts(Sheet sheet) {
        Iterator<Row> rowIterator = sheet.iterator();

        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            String assure = ExcelCellUtil.getCellValue(row, 0, String.class);
            String societe = ExcelCellUtil.getCellValue(row, 1, String.class);
            String telephone = ExcelCellUtil.getCellValue(row, 2, String.class);
            String email = ExcelCellUtil.getCellValue(row, 3, String.class);
            String msh = ExcelCellUtil.getCellValue(row, 4, String.class);
            String motDePasse = ExcelCellUtil.getCellValue(row, 5, String.class);
            String cin = ExcelCellUtil.getCellValue(row, 6, String.class);
            String carteSejour = ExcelCellUtil.getCellValue(row, 7, String.class);

            if (ExcelRowUtil.isRowEmpty(assure, telephone, email, msh, motDePasse, cin, carteSejour)) {
                continue;
            }

            if (contactsRepository.existsByUniqueFields(assure, email, telephone, cin)) {
                logger.info("Skipping duplicate entry: " + assure);
            } else {

                ContactsDTO contactsDTO = new ContactsDTO(assure, societe, telephone, email, msh, motDePasse, cin,
                        carteSejour);

                Contacts contact = contactsMapper.toEntity(contactsDTO);

                contactsRepository.save(contact);
            }
        }
    }

    public Optional<Contacts> findByAssure(String assure) {
        return contactsRepository.findByAssure(assure);
    }

    public Optional<Contacts> findBySociete(String societe) {
        return contactsRepository.findBySociete(societe);
    }

    @Override
    public List<ContactsDTO> getAllContacts() {
        List<Contacts> contacts = contactsRepository.findAll();
        return contacts.stream().map(contactsMapper::toDTO).toList();
    }

    @Override
    public List<ContactsDTO> searchContacts(String assure, String msh, String societe,
            Integer codeRisque, String numeroContrat) {
        List<ContactsDTO> contacts = new ArrayList<>();

        List<ContactsDTO> contactDtos = contactsRepository
                .searchContacts(assure, msh, societe, codeRisque, numeroContrat)
                .stream()
                .map(contact -> new ContactsDTO(
                        contact.getAssure(),
                        contact.getSociete(),
                        contact.getTelephone(),
                        contact.getEmail(),
                        contact.getMsh(),
                        contact.getMotDePasse(),
                        contact.getCin(),
                        contact.getCarteSejour()))
                .toList();
        contacts.addAll(contactDtos);
        return contacts;
    }

    @Override
    public Contacts saveContact(CreateContactDto contactDTO) {

        Contacts contact = new Contacts();

        contact.setAssure(contactDTO.getAssure());
        contact.setSociete(contactDTO.getSociete());
        contact.setTelephone(contactDTO.getTelephone());
        contact.setEmail(contactDTO.getEmail());
        contact.setMsh(contactDTO.getMsh());
        contact.setMotDePasse(contactDTO.getMotDePasse());
        contact.setCin(contactDTO.getCin());
        contact.setCarteSejour(contactDTO.getCarteSejour());

        // Save contact first to generate an ID
        Contacts savedContact = contactsRepository.save(contact);

        // Convert DTO Contracts to EntitiesProduction contract = new Production();
        Production contract = new Production();
        Optional<Risque> risqueOpt = risqueServiceImpl
                .findBycodeRisque(contactDTO.getContracts().get(0).getCodeRisque());

        if (risqueOpt.isPresent()) {
            Risque risque = risqueOpt.get();

            contract.setNumeroContrat(generateUniqueContractNumber());
            contract.setNature(contactDTO.getContracts().get(0).getNature());
            contract.setRisque(risque);
            contract.setDateEffet(contactDTO.getContracts().get(0).getDateEffet());
            contract.setDateEcheance(contactDTO.getContracts().get(0).getDateEcheance());
            contract.setDureeContrat(contactDTO.getContracts().get(0).getDureeContrat());
            contract.setModePayement(contactDTO.getContracts().get(0).getModePayement());
            contract.setNombreCheque(contactDTO.getContracts().get(0).getNombreCheque());
            contract.setNumeroCheque(contactDTO.getContracts().get(0).getNumeroCheque());
            contract.setPrimeNette(contactDTO.getContracts().get(0).getPrimeNette());
            contract.setPrime(contactDTO.getContracts().get(0).getPrime());
            contract.setCommission(contactDTO.getContracts().get(0).getCommission());
            contract.setRemarques(contactDTO.getContracts().get(0).getRemarques());
            contract.setContact(savedContact); // Link contract to saved contact

            productionRepository.save(contract); // Save contract
        } else {
            throw new RuntimeException("Risque not found");
        }

        // Convert DTO Banques to Entities (One transaction in this case)
        Banque banque = new Banque();
        banque.setDate(contactDTO.getTransactions().get(0).getDate());
        banque.setMontant(contactDTO.getTransactions().get(0).getMontant());
        banque.setTerme(contactDTO.getTransactions().get(0).getTerme());
        banque.setNt(contactDTO.getTransactions().get(0).getNt());
        banque.setContact(savedContact); // Link banque to saved contact

        // Link the Banque to the contract via the contract number (produced above)
        banque.setContract(contract); // Link to the contract created earlier

        banqueRepository.save(banque); // Save transaction (banque)

        return savedContact;
    }

    public String generateUniqueContractNumber() {
        String contractNumber;
        do {
            contractNumber = ContractNumberUtil.generateContractNumber(); // Generates contract number
        } while (productionRepository.existsByNumeroContrat(contractNumber)); // Ensures uniqueness
        return contractNumber;
    }

}