package com.maghrebia.data_extract.Business.ServicesImpl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
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

import com.maghrebia.data_extract.Business.Services.ContactsService;
import com.maghrebia.data_extract.DAO.Entities.Banque;
import com.maghrebia.data_extract.DAO.Entities.Contacts;
import com.maghrebia.data_extract.DAO.Entities.Production;
import com.maghrebia.data_extract.DAO.Entities.Risque;
import com.maghrebia.data_extract.DAO.Repositories.BanqueRepository;
import com.maghrebia.data_extract.DAO.Repositories.ContactsRepository;
import com.maghrebia.data_extract.DAO.Repositories.ProductionRepository;
import com.maghrebia.data_extract.DTO.ContactsDTO;
import com.maghrebia.data_extract.DTO.CreateBanqueDTO;
import com.maghrebia.data_extract.DTO.CreateContactDto;
import com.maghrebia.data_extract.DTO.CreateProductionDTO;
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
            String passeport = ExcelCellUtil.getCellValue(row, 8, String.class);
            String matriculeFiscale = ExcelCellUtil.getCellValue(row, 9, String.class);

            if (ExcelRowUtil.isRowEmpty(assure, telephone, email, msh, motDePasse, cin, carteSejour,
                    passeport, matriculeFiscale)) {
                continue;
            }

            if (contactsRepository.existsByUniqueFields(assure, email, telephone, cin)) {
                logger.info("Skipping duplicate entry: " + assure);
            } else {

                ContactsDTO contactsDTO = new ContactsDTO(assure, societe, telephone, email, msh,
                        motDePasse, cin, carteSejour, passeport, matriculeFiscale);

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
                        contact.getIdContact(),
                        contact.getAssure(),
                        contact.getSociete(),
                        contact.getTelephone(),
                        contact.getEmail(),
                        contact.getMsh(),
                        contact.getMotDePasse(),
                        contact.getCin(),
                        contact.getCarteSejour(),
                        contact.getPasseport(),
                        contact.getMatriculeFiscale()))
                .toList();
        contacts.addAll(contactDtos);
        return contacts;
    }

    @Override
    public Contacts saveContact(CreateContactDto contactDTO) {

        Optional<Contacts> existing = contactsRepository.findByEmail(contactDTO.getEmail());

        if (existing.isPresent()) {
            throw new RuntimeException("A contact with this email already exists.");
        }

        Contacts contact = new Contacts();

        contact.setAssure(contactDTO.getAssure());
        contact.setSociete(contactDTO.getSociete());
        contact.setTelephone(contactDTO.getTelephone());
        contact.setEmail(contactDTO.getEmail());
        contact.setMsh(contactDTO.getMsh());
        contact.setMotDePasse(contactDTO.getMotDePasse());
        contact.setCin(contactDTO.getCin());
        contact.setCarteSejour(contactDTO.getCarteSejour());
        contact.setPasseport(contactDTO.getPasseport());
        contact.setMatriculeFiscale(contactDTO.getMatriculeFiscale());

        // Save contact first to generate an ID
        Contacts savedContact = contactsRepository.save(contact);

        for (CreateProductionDTO contractDto : contactDTO.getContracts()) {
            Optional<Risque> risqueOpt = risqueServiceImpl.findByidRisque(contractDto.getCodeRisque());

            if (risqueOpt.isPresent()) {
                Risque risque = risqueOpt.get();
                Production contract = new Production();

                contract.setNumeroContrat(generateUniqueContractNumber());
                contract.setNature(contractDto.getNature());
                contract.setRisque(risque);
                contract.setDateEffet(contractDto.getDateEffet());
                contract.setDateEcheance(contractDto.getDateEcheance());
                contract.setMois(contractDto.getMois());
                contract.setDureeContrat(contractDto.getDureeContrat());
                contract.setModePayement(contractDto.getModePayement());
                contract.setNombreCheque(contractDto.getNombreCheque());
                contract.setNumeroCheque(contractDto.getNumeroCheque());
                contract.setDateDuCheque(contractDto.getDateDuCheque());
                contract.setPrimeNette(contractDto.getPrimeNette());
                contract.setPrime(contractDto.getPrime());
                contract.setCommission(contractDto.getCommission());
                contract.setRemarques(contractDto.getRemarques());
                contract.setContact(savedContact);

                Production savedContract = productionRepository.save(contract);

                if (contractDto.getTransactions() != null) {
                    for (CreateBanqueDTO banqueDto : contractDto.getTransactions()) {
                        Banque banque = new Banque();
                        banque.setDate(banqueDto.getDate());
                        banque.setMontant(banqueDto.getMontant());
                        banque.setTerme(banqueDto.getTerme());
                        banque.setModePayement(banqueDto.getModePayement());
                        banque.setNt(banqueDto.getNt());
                        banque.setBvBanque(banqueDto.getBvBanque());
                        banque.setBvPortail(banqueDto.getBvPortail());
                        banque.setRemarque(banqueDto.getRemarque());
                        banque.setContact(savedContact);
                        banque.setContract(savedContract);

                        banqueRepository.save(banque);
                    }
                }
            } else {
                throw new RuntimeException("Risque not found for code: " + contractDto.getCodeRisque());
            }
        }

        return savedContact;
    }

    public String generateUniqueContractNumber() {
        String contractNumber;
        do {
            contractNumber = ContractNumberUtil.generateContractNumber(); // Generates contract number
        } while (productionRepository.existsByNumeroContrat(contractNumber)); // Ensures uniqueness
        return contractNumber;
    }

    @Override
    @Transactional
    public String deleteContact(Long idContact) {
        try {
            if (!contactsRepository.existsById(idContact)) {
                throw new RuntimeException("Contact with id " + idContact +
                        " not found");
            }
            banqueRepository.deleteByContactId(idContact);
            productionRepository.deleteByContactId(idContact);
            contactsRepository.deleteById(idContact);
            return "Contact deleted successfully";

        } catch (Exception e) {
            logger.error("Error while deleting contact with id " + idContact, e);
            throw new RuntimeException("Error while deleting contact with id " +
                    idContact + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void updateContact(Long idContact, ContactsDTO contactDTO) {
        contactsRepository.findById(idContact)
                .ifPresent(currentContact -> {
                    currentContact.setAssure(contactDTO.getAssure());
                    currentContact.setSociete(contactDTO.getSociete());
                    currentContact.setTelephone(contactDTO.getTelephone());
                    currentContact.setEmail(contactDTO.getEmail());
                    currentContact.setMsh(contactDTO.getMsh());
                    currentContact.setMotDePasse(contactDTO.getMotDePasse());
                    currentContact.setCin(contactDTO.getCin());
                    currentContact.setCarteSejour(contactDTO.getCarteSejour());
                    currentContact.setPasseport(contactDTO.getPasseport());
                    currentContact.setMatriculeFiscale(contactDTO.getMatriculeFiscale());

                    contactsRepository.save(currentContact);
                });
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportContactToExcel() {
        List<Contacts> contacts = contactsRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Contacts");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Assuré");
            header.createCell(1).setCellValue("Société");
            header.createCell(2).setCellValue("Tél");
            header.createCell(3).setCellValue("E-mail");
            header.createCell(4).setCellValue("N MSH");
            header.createCell(5).setCellValue("Mot de passe");
            header.createCell(6).setCellValue("CIN");
            header.createCell(7).setCellValue("Catre séjour");
            header.createCell(8).setCellValue("Passeport");
            header.createCell(9).setCellValue("Matricule fiscale");

            int rowNum = 1;
            for (Contacts contact : contacts) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(contact.getAssure());
                row.createCell(1).setCellValue(contact.getSociete());
                row.createCell(2).setCellValue(contact.getTelephone());
                row.createCell(3).setCellValue(contact.getEmail());
                row.createCell(4).setCellValue(contact.getMsh());
                row.createCell(5).setCellValue(contact.getMotDePasse());
                row.createCell(6).setCellValue(contact.getCin());
                row.createCell(7).setCellValue(contact.getCarteSejour());
                row.createCell(8).setCellValue(contact.getPasseport());
                row.createCell(9).setCellValue(contact.getMatriculeFiscale());
            }

            workbook.write(outputStream);
            byte[] byteArray = outputStream.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(byteArray);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contacts.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    @Transactional
    public String deleteMultipleContacts(List<Long> ids) {
        for (Long id : ids) {
            deleteContact(id);
        }
        return "Selected contacts deleted successfully";
    }

}