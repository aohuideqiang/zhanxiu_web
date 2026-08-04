package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("company_website")
public class ServiceCard {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String iconUrl;

    private String title;

    private String description;

    private String targetLink;

    public ServiceCard() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetLink() {
        return targetLink;
    }

    public void setTargetLink(String targetLink) {
        this.targetLink = targetLink;
    }
}