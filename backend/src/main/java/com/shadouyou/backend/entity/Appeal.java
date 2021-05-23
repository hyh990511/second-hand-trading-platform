package com.shadouyou.backend.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Appeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appealid;
    private Integer orderid;
    private Integer userid;
    private String appealtime;
    private String appealreason;
    private Integer state;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getAppealid() {
        return appealid;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public Integer getState() {
        return state;
    }

    public String getAppealreason() {
        return appealreason;
    }

    public String getAppealtime() {
        return appealtime;
    }

    public void setAppealid(Integer appealid) {
        this.appealid = appealid;
    }

    public void setAppealreason(String appealreason) {
        this.appealreason = appealreason;
    }

    public void setAppealtime(String appealtime) {
        this.appealtime = appealtime;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
