package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Footer;
import com.example.demo.mapper.FooterMapper;
import com.example.demo.service.FooterService;

@Service
public class FooterServiceImpl extends ServiceImpl<FooterMapper, Footer> implements FooterService {
}