package com.shadouyou.backend.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Order_ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderid;
    private Integer goodsid;
    private Double goodsprice;
    private String goodsname;
    private Integer userid;
    private String tradingstatus;
    private String releasetime;
    private String endtime;
    private Integer iscomment;
    private Double creditrating;
//    private String evaluation;
    private Integer sendway;
    private Double sendprice;
    private String imgfirst;
    private String imgsecond;
    private String imgthird;


    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public void setTradingstatus(String tradingstatus) {
        this.tradingstatus = tradingstatus;
    }

    public void setGoodsprice(Double goodsprice) {
        this.goodsprice = goodsprice;
    }

    public void setIscomment(Integer iscomment) {
        this.iscomment = iscomment;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getTradingstatus() {
        return tradingstatus;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setCreditrating(Double creditrating) {
        this.creditrating = creditrating;
    }

//    public void setEvaluation(String evaluation) {
//        this.evaluation = evaluation;
//    }

    public Integer getIscomment() {
        return iscomment;
    }

    public Double getCreditrating() {
        return creditrating;
    }
//
//    public String getEvaluation() {
//        return evaluation;
//    }

    public Double getGoodsprice() {
        return goodsprice;
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public Integer getUserid() {
        return userid;
    }

    public String getReleasetime() {
        return releasetime;
    }

    public void setSendprice(Double sendprice) {
        this.sendprice = sendprice;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public void setSendway(Integer sendway) {
        this.sendway = sendway;
    }

    public void setReleasetime(String releasetime) {
        this.releasetime = releasetime;
    }

    public Double getSendprice() {
        return sendprice;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getSendway() {
        return sendway;
    }

    public String getImgfirst() {
        return imgfirst;
    }

    public void setImgfirst(String imgfirst) {
        this.imgfirst = imgfirst;
    }

    public String getImgsecond() {
        return imgsecond;
    }

    public String getImgthird() {
        return imgthird;
    }

    public void setImgsecond(String imgsecond) {
        this.imgsecond = imgsecond;
    }

    public void setImgthird(String imgthird) {
        this.imgthird = imgthird;
    }

}
