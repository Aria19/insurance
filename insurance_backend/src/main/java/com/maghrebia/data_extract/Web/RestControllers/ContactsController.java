package com.maghrebia.data_extract.Web.RestControllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maghrebia.data_extract.Business.ServicesImpl.ContactsServiceImpl;
import com.maghrebia.data_extract.DAO.Entities.Contacts;
import com.maghrebia.data_extract.DTO.ContactsDTO;
import com.maghrebia.data_extract.DTO.CreateContactDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/contacts")
public class ContactsController {

    private final ContactsServiceImpl contactsServiceImpl;

    public ContactsController(ContactsServiceImpl contactsServiceImpl) {
        this.contactsServiceImpl = contactsServiceImpl;
    }
    
    @GetMapping("/view")
    public ResponseEntity<List<ContactsDTO>> getAllContacts(){
        return ResponseEntity.ok(contactsServiceImpl.getAllContacts());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ContactsDTO>> searchContacts(
            @RequestParam(value = "assure", required = false) String assure,
            @RequestParam(value = "msh", required = false) String msh,
            @RequestParam(value = "societe", required = false) String societe,
            @RequestParam(value = "codeRisque", required = false) Integer codeRisque,
            @RequestParam(value = "numeroContrat", required = false) String numeroContrat) {
        
        List<ContactsDTO> contacts = contactsServiceImpl.searchContacts(assure, msh, societe, codeRisque, numeroContrat);
        
        if (contacts.isEmpty()) {
            return ResponseEntity.noContent().build();  // No contacts found
        }
        
        return ResponseEntity.ok(contacts);  // Return the list of contacts
    }

    @PostMapping("/add")
    public ResponseEntity<Contacts> addContact(@RequestBody CreateContactDto createContactDto) {
        Contacts savedContact = contactsServiceImpl.saveContact(createContactDto);
        return new ResponseEntity<>(savedContact, HttpStatus.CREATED);
    }

    @PutMapping("update/{idContact}")
    public ResponseEntity<String> updateContact(@PathVariable Long idContact, @RequestBody ContactsDTO contactsDTO) {
        contactsServiceImpl.updateContact(idContact, contactsDTO);
        return ResponseEntity.ok("Contact updated successfully");
    }
    
    @DeleteMapping("/delete/{idContact}")
    public ResponseEntity<String> deleteContact(@PathVariable Long idContact){
        try {
            contactsServiceImpl.deleteContact(idContact);
            return ResponseEntity.ok("Contact with Id: " + idContact + " has been deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
