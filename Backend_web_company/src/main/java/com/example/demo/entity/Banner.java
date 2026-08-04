package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("banner")
public class Banner {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "图片链接不能为空")
    @Size(max = 500, message = "图片链接长度不能超过500个字符")
    private String imageUrl;   // 图片链接

    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题长度不能超过100个字符")
    private String title;      // 标题

    @Size(max = 200, message = "副标题长度不能超过200个字符")
    private String subtitle;   // 副标题

    @Size(max = 500, message = "跳转链接长度不能超过500个字符")
    private String linkUrl;    // 跳转链接

    @PositiveOrZero(message = "排序号不能为负数")
    private Integer sortOrder; // 排序号
}
