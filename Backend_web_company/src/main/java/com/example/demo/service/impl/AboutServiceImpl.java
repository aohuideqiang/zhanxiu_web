package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.About;
import com.example.demo.mapper.AboutMapper;
import com.example.demo.service.AboutService;

@Service
public class AboutServiceImpl extends ServiceImpl<AboutMapper, About> implements AboutService {
}