package com.shadouyou.backend.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentid;
    private Integer userid;
    private Integer goodsid;
    private String releasetime;
    private String text;

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getCommentid() {
        return commentid;
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

    public String getText() {
        return text;
    }

    public void setCommentid(Integer commentid) {
        this.commentid = commentid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public void setReleasetime(String releasetime) {
        this.releasetime = releasetime;
    }

    public void setText(String text) {
        this.text = text;
    }

}
