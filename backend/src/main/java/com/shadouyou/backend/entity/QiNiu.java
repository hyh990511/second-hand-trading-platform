package com.shadouyou.backend.entity;

import lombok.Data;

@Data
public class QiNiu {
    private String token;
    private String key;

    public void setToken(String token) {
        this.token = token;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToken() {
        return token;
    }

    public String getKey() {
        return key;
    }


}
