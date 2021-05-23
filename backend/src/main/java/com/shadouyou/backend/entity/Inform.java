package com.shadouyou.backend.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Inform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer informid;
    private Integer objectid;
    private Integer userid;
    private Integer type;
    private String reason;
    private Integer state;
    private String releasetime;

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return state;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setReleasetime(String releasetime) {
        this.releasetime = releasetime;
    }

    public String getReleasetime() {
        return releasetime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getInformid() {
        return informid;
    }

    public Integer getObjectid() {
        return objectid;
    }

    public String getReason() {
        return reason;
    }

    public void setInformid(Integer informid) {
        this.informid = informid;
    }

    public void setObjectid(Integer objectid) {
        this.objectid = objectid;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
