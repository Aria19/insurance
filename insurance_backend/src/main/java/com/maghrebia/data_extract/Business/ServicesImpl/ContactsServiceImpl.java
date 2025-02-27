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
import com.maghrebia.data_extract.DAO.Entities.Contacts;
import com.maghrebia.data_extract.DAO.Repositories.ContactsRepository;
import com.maghrebia.data_extract.DTO.ClientWithContractsDTO;
import com.maghrebia.data_extract.DTO.ContactsDTO;
import com.maghrebia.data_extract.Mapper.ContactsMapper;
import com.maghrebia.data_extract.Utils.ExcelCellUtil;
import com.maghrebia.data_extract.Utils.ExcelRowUtil;

@Service
public class ContactsServiceImpl implements ContactsService {

    private final ContactsRepository contactsRepository;
    private final ContactsMapper contactsMapper;
    private final Logger logger = LoggerFactory.getLogger(ContactsServiceImpl.class);

    public ContactsServiceImpl(ContactsRepository contactsRepository,
            ContactsMapper contactsMapper) {
        this.contactsRepository = contactsRepository;
        this.contactsMapper = contactsMapper;
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
    public void addClientWithContracts(ClientWithContractsDTO clientAndContractsDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addClientWithContracts'");
    }

}