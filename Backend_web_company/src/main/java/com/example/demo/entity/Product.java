package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "产品名称不能为空")
    @Size(max = 100, message = "产品名称长度不能超过100个字符")
    private String name;        // 产品名称

    @NotBlank(message = "产品描述不能为空")
    private String description; // 描述

    @Size(max = 500, message = "图片链接长度不能超过500个字符")
    private String imageUrl;    // 图片链接

    @NotBlank(message = "价格不能为空")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "价格格式不正确")
    private String price;       // 价格
}
