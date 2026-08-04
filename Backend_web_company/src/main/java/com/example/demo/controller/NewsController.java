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

import com.example.demo.entity.News;
import com.example.demo.service.NewsService;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public List<News> getAllNews() {
        return newsService.list();
    }

    @GetMapping("/{id}")
    public News getNewsById(@PathVariable Long id) {
        return newsService.getById(id);
    }

    @PostMapping
    public boolean createNews(@RequestBody News news) {
        return newsService.save(news);
    }

    @PutMapping("/{id}")
    public boolean updateNews(@PathVariable Long id, @RequestBody News updatedNews) {
        updatedNews.setId(id);
        return newsService.saveOrUpdate(updatedNews);
    }

    @DeleteMapping("/{id}")
    public boolean deleteNews(@PathVariable Long id) {
        return newsService.removeById(id);
    }
}