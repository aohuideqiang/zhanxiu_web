package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Banner;
import com.example.demo.mapper.BannerMapper;
import com.example.demo.service.BannerService;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Override
    @SuppressWarnings("null")
    public List<Banner> listOrdered() {
        return lambdaQuery()
        
                .orderByAsc(Banner::getSortOrder)
                .orderByAsc(Banner::getId)
                .list();
    }
}