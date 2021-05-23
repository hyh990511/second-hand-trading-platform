package com.shadouyou.backend.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagid;
    private String ascription;
    private String tagname;
    private Integer hot;

    public String getAscription() {
        return ascription;
    }

    public void setAscription(String ascription) {
        this.ascription = ascription;
    }

    public Integer getHot() {
        return hot;
    }

    public Integer getTagid() {
        return tagid;
    }

    public String getTagname() {
        return tagname;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public void setTagid(Integer tagid) {
        this.tagid = tagid;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }
}
