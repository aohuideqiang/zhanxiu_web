package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("solution")
public class Solution {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String solutionKey;   // 唯一标识，如 automotive、finance

    private String title;          // 方案标题

    private String description;    // 方案描述

    private String tags;           // 标签（逗号分隔）

    private String iconUrl;        // icon 图标（base64 或 URL）

    private String imageUrl;       // 图片（base64 或 URL）

    public Solution() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSolutionKey() { return solutionKey; }
    public void setSolutionKey(String solutionKey) { this.solutionKey = solutionKey; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}