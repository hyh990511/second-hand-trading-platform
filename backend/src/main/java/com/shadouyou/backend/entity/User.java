package com.shadouyou.backend.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;
    private String accountnumber;
    private String password;
    private String nickname;
    private String gender;
    private String introduction;
//    private Integer administratorid;
    private String realname;
    private String email;
    private String phone;
    private String qqnumber;
    private String address;
    private Double creditrating;
//    private Integer commentnumber;
    private Integer isuse;
    private String images;

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Integer getUserid() {
        return userid;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

//    public Integer getAdministratorid() {
//        return administratorid;
//    }

//    public void setAdministratorid(Integer administratorid) {
//        this.administratorid = administratorid;
//    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQqnumber() {
        return qqnumber;
    }

    public void setQqnumber(String qqnumber) {
        this.qqnumber = qqnumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getCreditrating() {
        return creditrating;
    }

    public void setCreditrating(Double creaditrating) {
        this.creditrating = creaditrating;
    }

//    public Integer getCommentnumber() {
//        return commentnumber;
//    }
//
//    public void setCommentnumber(Integer commentnumber) {
//        this.commentnumber = commentnumber;
//    }

    public Integer getIsuse() {
        return isuse;
    }

    public void setIsuse(Integer isuse) {
        this.isuse = isuse;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

}
