package com.shadouyou.backend.entity;

import lombok.Data;

@Data
public class AdmiPage {
    private Integer administratorid;
    private String account;
    private Integer accesslevel;
    private String nickname;
    public AdmiPage(Administrator a) {
        this.administratorid = a.getAdministratorid();
        this.account = a.getAccount();
        this.accesslevel = a.getAccesslevel();
        this.nickname = a.getNickname();
    }
}
