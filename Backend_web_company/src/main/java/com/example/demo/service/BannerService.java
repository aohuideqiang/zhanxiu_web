package com.example.demo.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Banner;

public interface BannerService extends IService<Banner> {
    List<Banner> listOrdered();
}