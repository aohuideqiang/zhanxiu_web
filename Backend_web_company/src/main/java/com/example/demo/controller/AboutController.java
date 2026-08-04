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

import com.example.demo.entity.About;
import com.example.demo.service.AboutService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/abouts")
@CrossOrigin(origins = "*")
public class AboutController {

    private final AboutService aboutService;

    public AboutController(AboutService aboutService) {
        this.aboutService = aboutService;
    }

    @GetMapping
    public SaResult getAllAbouts() {
        return SaResult.data(aboutService.list());
    }

    @GetMapping("/{id}")
    public SaResult getAboutById(@PathVariable Long id) {
        About about = aboutService.getById(id);
        return about == null ? SaResult.error("关于信息不存在").setCode(404) : SaResult.data(about);
    }

    @PostMapping
    public SaResult createAbout(@Valid @RequestBody About about) {
        return aboutService.save(about) ? SaResult.ok("创建关于信息成功") : SaResult.error("创建关于信息失败");
    }

    @PutMapping("/{id}")
    public SaResult updateAbout(@PathVariable Long id, @Valid @RequestBody About updatedAbout) {
        updatedAbout.setId(id);
        return aboutService.saveOrUpdate(updatedAbout) ? SaResult.ok("更新关于信息成功") : SaResult.error("更新关于信息失败");
    }

    @DeleteMapping("/{id}")
    public SaResult deleteAbout(@PathVariable Long id) {
        return aboutService.removeById(id) ? SaResult.ok("删除关于信息成功") : SaResult.error("关于信息不存在或已删除").setCode(404);
    }
}
