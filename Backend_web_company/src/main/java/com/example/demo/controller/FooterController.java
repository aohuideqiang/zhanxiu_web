package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Footer;
import com.example.demo.service.FooterService;

@RestController
@RequestMapping("/api/footer")
@CrossOrigin(origins = "*")
public class FooterController {

    private final FooterService footerService;

    public FooterController(FooterService footerService) {
        this.footerService = footerService;
    }

    // 查询（获取唯一一条）
    @GetMapping
    public Footer getFooter() {
        Footer footer = footerService.getOne(
            new QueryWrapper<Footer>().last("LIMIT 1")
        );
        if (footer == null) {
            footer = new Footer();
            footer.setBrand("广西展秀信息科技有限公司");
            footer.setSlogan("高质量AI数据");
            footer.setAddress("南宁市高新区科园大道95号");
            footer.setPhone("400-888-1234");
            footer.setEmail("admin@zanxiu.com");
            footer.setQrcodeTip("友情链接：云测平台");
            footer.setCopyright("Copyright © 2026 广西展秀信息科技有限公司. All rights reserved.");
            footer.setNavGroups("[{\"title\":\"产品服务\",\"links\":[\"数据采集\",\"数据标注\",\"质量质检\",\"平台部署\"]},{\"title\":\"解决方案\",\"links\":[\"智能驾驶\",\"智能家居\",\"智能安防\",\"智能金融\",\"新零售\"]},{\"title\":\"资源与生态\",\"links\":[\"开发文档\",\"技术博客\",\"合作伙伴\"]},{\"title\":\"关于我们\",\"links\":[\"公司简介\",\"加入我们\",\"媒体报道\",\"商务合作\"]}]");
            footerService.save(footer);
        }
        return footer;
    }

    // 新增
    @PostMapping
    public boolean createFooter(@RequestBody Footer footer) {
        return footerService.save(footer);
    }

    // 更新
    @PutMapping("/{id}")
    public boolean updateFooter(@PathVariable Long id, @RequestBody Footer footer) {
        footer.setId(id);
        return footerService.saveOrUpdate(footer);
    }

    // 删除
    @DeleteMapping("/{id}")
    public boolean deleteFooter(@PathVariable Long id) {
        return footerService.removeById(id);
    }
}