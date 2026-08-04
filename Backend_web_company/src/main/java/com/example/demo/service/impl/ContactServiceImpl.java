package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Contact;
import com.example.demo.mapper.ContactMapper;
import com.example.demo.service.ContactService;

@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact> implements ContactService {
}