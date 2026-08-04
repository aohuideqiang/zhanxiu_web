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

import com.example.demo.entity.ServiceCard;
import com.example.demo.service.ServiceCardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "*")
public class ServiceCardController {

    private final ServiceCardService serviceCardService;

    public ServiceCardController(ServiceCardService serviceCardService) {
        this.serviceCardService = serviceCardService;
    }

    @GetMapping
    public SaResult getAllCards() {
        return SaResult.data(serviceCardService.list());
    }

    @GetMapping("/{id}")
    public SaResult getCardById(@PathVariable Long id) {
        ServiceCard serviceCard = serviceCardService.getById(id);
        return serviceCard == null ? SaResult.error("服务卡片不存在").setCode(404) : SaResult.data(serviceCard);
    }

    @PostMapping
    public SaResult createCard(@Valid @RequestBody ServiceCard serviceCard) {
        return serviceCardService.save(serviceCard) ? SaResult.ok("创建服务卡片成功") : SaResult.error("创建服务卡片失败");
    }

    @PutMapping("/{id}")
    public SaResult updateCard(@PathVariable Long id, @Valid @RequestBody ServiceCard updatedCard) {
        updatedCard.setId(id);
        return serviceCardService.saveOrUpdate(updatedCard) ? SaResult.ok("更新服务卡片成功") : SaResult.error("更新服务卡片失败");
    }

    @DeleteMapping("/{id}")
    public SaResult deleteCard(@PathVariable Long id) {
        return serviceCardService.removeById(id) ? SaResult.ok("删除服务卡片成功") : SaResult.error("服务卡片不存在或已删除").setCode(404);
    }
}
