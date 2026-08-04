package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.News;
import com.example.demo.mapper.NewsMapper;
import com.example.demo.service.NewsService;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
}