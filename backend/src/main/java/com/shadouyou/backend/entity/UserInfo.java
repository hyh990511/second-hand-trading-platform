package com.shadouyou.backend.entity;

import lombok.Data;

@Data
public class UserInfo {
    private Integer userid;
    private String accountnumber;
    private String nickname;
    private String gender;
    private String introduction;
    private String realname;
    private String email;
    private String phone;
    private String qqnumber;
    private String address;
    private Double creditrating;
    private String images;

    public UserInfo (User user) {
        this.userid = user.getUserid();
        this.accountnumber = user.getAccountnumber();
        this.nickname = user.getNickname();
        this.gender = user.getGender();
        this.introduction = user.getIntroduction();
        this.realname = user.getRealname();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.qqnumber = user.getQqnumber();
        this.address = user.getAddress();
        this.creditrating = user.getCreditrating();
        this.images = user.getImages();
    }
}
