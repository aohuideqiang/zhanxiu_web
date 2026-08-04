package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.ServiceCard;
import com.example.demo.mapper.ServiceCardMapper;
import com.example.demo.service.ServiceCardService;

@Service
public class ServiceCardServiceImpl extends ServiceImpl<ServiceCardMapper, ServiceCard> implements ServiceCardService {
}