package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Solution;
import com.example.demo.mapper.SolutionMapper;
import com.example.demo.service.SolutionService;

@Service
public class SolutionServiceImpl extends ServiceImpl<SolutionMapper, Solution> implements SolutionService {
}