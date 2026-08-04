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

import com.example.demo.entity.About;
import com.example.demo.service.AboutService;

@RestController
@RequestMapping("/api/abouts")
@CrossOrigin(origins = "*")
public class AboutController {

    private final AboutService aboutService;

    public AboutController(AboutService aboutService) {
        this.aboutService = aboutService;
    }

    @GetMapping
    public List<About> getAllAbouts() {
        return aboutService.list();
    }

    @GetMapping("/{id}")
    public About getAboutById(@PathVariable Long id) {
        return aboutService.getById(id);
    }

    @PostMapping
    public boolean createAbout(@RequestBody About about) {
        return aboutService.save(about);
    }

    @PutMapping("/{id}")
    public boolean updateAbout(@PathVariable Long id, @RequestBody About updatedAbout) {
        updatedAbout.setId(id);
        return aboutService.saveOrUpdate(updatedAbout);
    }

    @DeleteMapping("/{id}")
    public boolean deleteAbout(@PathVariable Long id) {
        return aboutService.removeById(id);
    }
}