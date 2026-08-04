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

import com.example.demo.entity.News;
import com.example.demo.service.NewsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public SaResult getAllNews() {
        return SaResult.data(newsService.list());
    }

    @GetMapping("/{id}")
    public SaResult getNewsById(@PathVariable Long id) {
        News news = newsService.getById(id);
        return news == null ? SaResult.error("新闻不存在").setCode(404) : SaResult.data(news);
    }

    @PostMapping
    public SaResult createNews(@Valid @RequestBody News news) {
        return newsService.save(news) ? SaResult.ok("创建新闻成功") : SaResult.error("创建新闻失败");
    }

    @PutMapping("/{id}")
    public SaResult updateNews(@PathVariable Long id, @Valid @RequestBody News updatedNews) {
        updatedNews.setId(id);
        return newsService.saveOrUpdate(updatedNews) ? SaResult.ok("更新新闻成功") : SaResult.error("更新新闻失败");
    }

    @DeleteMapping("/{id}")
    public SaResult deleteNews(@PathVariable Long id) {
        return newsService.removeById(id) ? SaResult.ok("删除新闻成功") : SaResult.error("新闻不存在或已删除").setCode(404);
    }
}
