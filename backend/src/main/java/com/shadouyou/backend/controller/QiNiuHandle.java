package com.shadouyou.backend.controller;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.shadouyou.backend.entity.QiNiu;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/QiNiu")
public class QiNiuHandle {
    @PostMapping("/getToken/{bucket}")
    public QiNiu getToken(@PathVariable("bucket") String bucket) {
        QiNiu qiNiu = new QiNiu();
        String accessKey = "0_Zt4ujJVFDZ0tHKdCD2QQA3-hcGxgL6CidSjGWS";
        String secretKey = "CaoRJOVJNsBtgcJ8d1ExPZbpzjTcqNpDqO_bzo_K";
        long expireSeconds = 600;   //过期时间
        StringMap putPolicy = new StringMap();
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket,null, expireSeconds,putPolicy);
        qiNiu.setKey(UUID.randomUUID().toString().replaceAll("\\-", ""));
        qiNiu.setToken(upToken);
        return qiNiu;
    }

}
