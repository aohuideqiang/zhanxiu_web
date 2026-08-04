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

import com.example.demo.entity.Banner;
import com.example.demo.service.BannerService;

@RestController
@RequestMapping("/api/banners")
@CrossOrigin(origins = "*")
public class BannerController {

    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping
    public List<Banner> getAllBanners() {
        return bannerService.listOrdered();
    }

    @GetMapping("/{id}")
    public Banner getBannerById(@PathVariable Long id) {
        return bannerService.getById(id);
    }

    @PostMapping
    public boolean createBanner(@RequestBody Banner banner) {
        return bannerService.save(banner);
    }

    @PutMapping("/{id}")
    public boolean updateBanner(@PathVariable Long id, @RequestBody Banner updatedBanner) {
        updatedBanner.setId(id);
        return bannerService.saveOrUpdate(updatedBanner);
    }

    @DeleteMapping("/{id}")
    public boolean deleteBanner(@PathVariable Long id) {
        return bannerService.removeById(id);
    }
}