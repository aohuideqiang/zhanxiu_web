package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("footer")
public class Footer {

    @TableId(type = IdType.AUTO)
    private Long id;

    @Size(max = 500, message = "Logo长度不能超过500个字符")
    private String logo;

    @NotBlank(message = "品牌名称不能为空")
    @Size(max = 100, message = "品牌名称长度不能超过100个字符")
    private String brand;

    @Size(max = 200, message = "标语长度不能超过200个字符")
    private String slogan;

    @Size(max = 200, message = "地址长度不能超过200个字符")
    private String address;

    @Pattern(regexp = "^[0-9+\\-()\\s]{5,30}$", message = "电话格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    @NotBlank(message = "导航分组不能为空")
    private String navGroups;

    @Size(max = 500, message = "二维码链接长度不能超过500个字符")
    private String qrcode;

    @Size(max = 100, message = "二维码提示长度不能超过100个字符")
    private String qrcodeTip;

    @Size(max = 200, message = "版权信息长度不能超过200个字符")
    private String copyright;
}
