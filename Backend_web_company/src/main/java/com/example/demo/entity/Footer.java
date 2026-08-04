package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

    public Footer() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getSlogan() { return slogan; }
    public void setSlogan(String slogan) { this.slogan = slogan; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNavGroups() { return navGroups; }
    public void setNavGroups(String navGroups) { this.navGroups = navGroups; }

    public String getQrcode() { return qrcode; }
    public void setQrcode(String qrcode) { this.qrcode = qrcode; }

    public String getQrcodeTip() { return qrcodeTip; }
    public void setQrcodeTip(String qrcodeTip) { this.qrcodeTip = qrcodeTip; }

    public String getCopyright() { return copyright; }
    public void setCopyright(String copyright) { this.copyright = copyright; }
}
