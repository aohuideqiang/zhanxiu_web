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

import com.example.demo.entity.ServiceCard;
import com.example.demo.service.ServiceCardService;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "*")
public class ServiceCardController {

    private final ServiceCardService serviceCardService;

    public ServiceCardController(ServiceCardService serviceCardService) {
        this.serviceCardService = serviceCardService;
    }

    @GetMapping
    public List<ServiceCard> getAllCards() {
        return serviceCardService.list();
    }

    @GetMapping("/{id}")
    public ServiceCard getCardById(@PathVariable Long id) {
        return serviceCardService.getById(id);
    }

    @PostMapping
    public boolean createCard(@RequestBody ServiceCard serviceCard) {
        return serviceCardService.save(serviceCard);
    }

    @PutMapping("/{id}")
    public boolean updateCard(@PathVariable Long id, @RequestBody ServiceCard updatedCard) {
        updatedCard.setId(id);
        return serviceCardService.saveOrUpdate(updatedCard);
    }

    @DeleteMapping("/{id}")
    public boolean deleteCard(@PathVariable Long id) {
        return serviceCardService.removeById(id);
    }
}