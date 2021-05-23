package com.shadouyou.backend.entity;

import lombok.Data;

@Data
public class UserPage {
    private Integer userid;
    private String accountnumber;
    private String nickname;
    private String gender;
    private Integer isuse;
    public UserPage(User user) {
        this.userid = user.getUserid();
        this.accountnumber = user.getAccountnumber();
        this.nickname = user.getNickname();
        this.gender = user.getGender();
        this.isuse = user.getIsuse();
    }
}
