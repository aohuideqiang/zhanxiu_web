package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Contact;
import com.example.demo.service.ContactService;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public List<Contact> getAllContacts() {
        return contactService.list();
    }

    @GetMapping("/{id}")
    public Contact getContactById(@PathVariable Long id) {
        return contactService.getById(id);
    }

    @PostMapping
    public boolean createContact(@RequestBody Contact contact) {
        return contactService.save(contact);
    }

    @PutMapping("/{id}")
    public boolean updateContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
        updatedContact.setId(id);
        return contactService.saveOrUpdate(updatedContact);
    }

    @DeleteMapping("/{id}")
    public boolean deleteContact(@PathVariable Long id) {
        return contactService.removeById(id);
    }
}