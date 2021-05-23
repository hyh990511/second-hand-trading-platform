package com.shadouyou.backend.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Data
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer goodsid;
    private String name;
    private String title;
    private String text;
    private Integer userid;
//    private String username;
    private double goodsprice;
    private String exchangebook;
    private String tag;
    private String childtag;
//    private String type;
    private double sendprice;
    private String realasetime;
//    private double discountprice;
    private Integer isexamine;
    private String examinetime;
//    private Integer ispass;
    private String ascription;
    private String nopassreason;
    private Integer issale;
//    private Integer administratorid;
    private String  imgfirst;
    private String imgsecond;
    private String imgthird;

//    public double getDiscountprice() {
//        return discountprice;
//    }

    public double getPrice() {
        return goodsprice;
    }

//    public Integer getAdministratorid() {
//        return administratorid;
//    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public Integer getIsexamine() {
        return isexamine;
    }

//    public Integer getIspass() {
//        return ispass;
//    }

    public Integer getIssale() {
        return issale;
    }

    public Integer getUserid() {
        return userid;
    }

    public String getExaminetime() {
        return examinetime;
    }

    public String getName() {
        return name;
    }



    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

//    public String getType() {
//        return type;
//    }



    public String getImgfirst() {
        return imgfirst;
    }

    public String getImgsecond() {
        return imgsecond;
    }

    public String getImgthird() {
        return imgthird;
    }

//    public void setAdministratorid(Integer administratorid) {
//        this.administratorid = administratorid;
//    }


    public void setExaminetime(String examinetime) {
        this.examinetime = examinetime;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public void setIsexamine(Integer isexamine) {
        this.isexamine = isexamine;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public void setIspass(Integer ispass) {
//        this.ispass = ispass;
//    }




    public void setPrice(double price) {
        this.goodsprice = price;
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public void setType(String type) {
//        this.type = type;
//    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }



    public void setImgfirst(String imgfirst) {
        this.imgfirst = imgfirst;
    }

    public void setImgsecond(String imgsecond) {
        this.imgsecond = imgsecond;
    }

    public void setImgthird(String imgthird) {
        this.imgthird = imgthird;
    }

    public String getNopassreason() {
        return nopassreason;
    }

    public String getRealasetime() {
        return realasetime;
    }

//    public void setDiscountprice(double discountprice) {
//        this.discountprice = discountprice;
//    }

    public void setIssale(Integer issale) {
        this.issale = issale;
    }

    public void setNopassreason(String nopassreason) {
        this.nopassreason = nopassreason;
    }

    public void setRealasetime(String realasetime) {
        this.realasetime = realasetime;
    }

//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getUsername() {
//        return username;
//    }


    public String getAscription() {
        return ascription;
    }

    public void setAscription(String ascription) {
        this.ascription = ascription;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getChildtag() {
        return childtag;
    }

    public void setChildtag(String childtag) {
        this.childtag = childtag;
    }

    public double getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(double goodsprice) {
        this.goodsprice = goodsprice;
    }

    public double getSendprice() {
        return sendprice;
    }

    public String getExchangebook() {
        return exchangebook;
    }

    public void setExchangebook(String exchangebook) {
        this.exchangebook = exchangebook;
    }

    public void setSendprice(double sendprice) {
        this.sendprice = sendprice;
    }

}