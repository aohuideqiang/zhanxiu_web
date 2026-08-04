package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("company_website")
public class ServiceCard {

    @TableId(type = IdType.AUTO)
    private Long id;

    @Size(max = 500, message = "图标链接长度不能超过500个字符")
    private String iconUrl;

    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题长度不能超过100个字符")
    private String title;

    @NotBlank(message = "描述不能为空")
    private String description;

    @Size(max = 500, message = "目标链接长度不能超过500个字符")
    private String targetLink;
}
