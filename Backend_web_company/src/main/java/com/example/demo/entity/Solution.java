package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("solution")
public class Solution {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "解决方案标识不能为空")
    @Pattern(regexp = "^[a-z0-9-]{2,50}$", message = "解决方案标识只能包含小写字母、数字和中划线")
    private String solutionKey;   // 唯一标识，如 automotive、finance

    @NotBlank(message = "方案标题不能为空")
    @Size(max = 100, message = "方案标题长度不能超过100个字符")
    private String title;          // 方案标题

    @NotBlank(message = "方案描述不能为空")
    private String description;    // 方案描述

    @Size(max = 300, message = "标签长度不能超过300个字符")
    private String tags;           // 标签（逗号分隔）

    @TableField("icon")
    @Size(max = 100, message = "图标长度不能超过100个字符")
    private String iconUrl;        // icon 图标（base64 或 URL）

    @TableField("image")
    @Size(max = 500, message = "图片链接长度不能超过500个字符")
    private String imageUrl;       // 图片（base64 或 URL）
}
