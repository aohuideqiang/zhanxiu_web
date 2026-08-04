package com.example.demo.controller;

import cn.dev33.satoken.util.SaResult;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public SaResult getAllContacts() {
        return SaResult.data(contactService.list());
    }

    @GetMapping("/{id}")
    public SaResult getContactById(@PathVariable Long id) {
        Contact contact = contactService.getById(id);
        return contact == null ? SaResult.error("联系信息不存在").setCode(404) : SaResult.data(contact);
    }

    @PostMapping
    public SaResult createContact(@Valid @RequestBody Contact contact) {
        return contactService.save(contact) ? SaResult.ok("创建联系信息成功") : SaResult.error("创建联系信息失败");
    }

    @PutMapping("/{id}")
    public SaResult updateContact(@PathVariable Long id, @Valid @RequestBody Contact updatedContact) {
        updatedContact.setId(id);
        return contactService.saveOrUpdate(updatedContact) ? SaResult.ok("更新联系信息成功") : SaResult.error("更新联系信息失败");
    }

    @DeleteMapping("/{id}")
    public SaResult deleteContact(@PathVariable Long id) {
        return contactService.removeById(id) ? SaResult.ok("删除联系信息成功") : SaResult.error("联系信息不存在或已删除").setCode(404);
    }
}
