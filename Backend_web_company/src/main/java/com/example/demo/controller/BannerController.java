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

import com.example.demo.entity.Banner;
import com.example.demo.service.BannerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/banners")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping
    public SaResult getAllBanners() {
        return SaResult.data(bannerService.listOrdered());
    }

    @GetMapping("/{id}")
    public SaResult getBannerById(@PathVariable Long id) {
        Banner banner = bannerService.getById(id);
        return banner == null ? SaResult.error("轮播图不存在").setCode(404) : SaResult.data(banner);
    }

    @PostMapping
    public SaResult createBanner(@Valid @RequestBody Banner banner) {
        return bannerService.save(banner) ? SaResult.ok("创建轮播图成功") : SaResult.error("创建轮播图失败");
    }

    @PutMapping("/{id}")
    public SaResult updateBanner(@PathVariable Long id, @Valid @RequestBody Banner updatedBanner) {
        updatedBanner.setId(id);
        return bannerService.saveOrUpdate(updatedBanner) ? SaResult.ok("更新轮播图成功") : SaResult.error("更新轮播图失败");
    }

    @DeleteMapping("/{id}")
    public SaResult deleteBanner(@PathVariable Long id) {
        return bannerService.removeById(id) ? SaResult.ok("删除轮播图成功") : SaResult.error("轮播图不存在或已删除").setCode(404);
    }
}
