package com.maghrebia.data_extract.Business.Services;

import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import com.maghrebia.data_extract.DAO.Entities.Contacts;
import com.maghrebia.data_extract.DTO.ContactsDTO;
import com.maghrebia.data_extract.DTO.CreateContactDto;

public interface ContactsService {

    public void importContacts(Sheet sheet);
    public Optional<Contacts> findByAssure(String assure);
    public Optional<Contacts> findBySociete(String societe);
    public List<ContactsDTO> getAllContacts();
    public List<ContactsDTO> searchContacts(String name, String msh, String societe, Integer codeRisque, String numeroContrat);
    public Contacts saveContact(CreateContactDto contactsDTO);
    public void updateContact(Long idContact, ContactsDTO contactDTO);
    public String deleteContact(Long idContact);
    public ResponseEntity<ByteArrayResource> exportContactToExcel();
    String deleteMultipleContacts(List<Long> ids);
    
}
