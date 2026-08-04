package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("footer")
public class Footer {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String logo;

    private String brand;

    private String slogan;

    private String address;

    private String phone;

    private String email;

    private String navGroups;

    private String qrcode;

    private String qrcodeTip;

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