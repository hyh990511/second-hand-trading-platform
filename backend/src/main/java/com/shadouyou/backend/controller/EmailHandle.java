package com.shadouyou.backend.controller;

import com.shadouyou.backend.entity.User;
import com.shadouyou.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/verify")
public class EmailHandle {
    private static final String KEY = "sha1dou2you3"; // KEY为自定义秘钥
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/email/{type}/{stuid}")
    public Object email(@PathVariable("type") Integer type,@PathVariable("stuid") String accountNumber){
        User user = userRepository.findUserByAccountnumber(accountNumber);
        if(type == 0){
            //注册用户
            if(user != null){
                return "account is occupied";
            }else{
                String code = VerifyCode(6);
                String sendTo = accountNumber+"@fzu.edu.cn";
                sendEmail(0,sendTo,code);
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MINUTE, 15);
                String currentTime = sf.format(c.getTime());// 生成15分钟后时间，用户校验是否过期

                String hash =  DigestUtils.md5DigestAsHex((KEY + "@" + currentTime + "@" + code).getBytes());//生成MD5值
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("hash", hash);
                resultMap.put("tamp", currentTime);
                //System.out.printf(code);
                return resultMap;
            }

        }else if(type == 1){
            //已注册用户忘记密码
            if(user == null){
                return "account is not exist";
            }else{
                String code = VerifyCode(6);
                String sendTo = accountNumber+"@fzu.edu.cn";

                sendEmail(1,sendTo,code);
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MINUTE, 15);
                String currentTime = sf.format(c.getTime());// 生成15分钟后时间，用户校验是否过期

                String hash =  DigestUtils.md5DigestAsHex((KEY + "@" + currentTime + "@" + code).getBytes());//生成MD5值
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("hash", hash);
                resultMap.put("tamp", currentTime);
                System.out.printf(code);
                return resultMap;
            }

        }else{
            //错误类型
            return "error";
        }
    }

    @PostMapping("/code/{verification}")
    public String VerifyCode(@RequestBody Map<String,Object> requestMap, @PathVariable("verification") String code){
        String requestHash = requestMap.get("hash").toString();
        String tamp = requestMap.get("tamp").toString();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");//当前时间
        Calendar c = Calendar.getInstance();
        String currentTime = sf.format(c.getTime());
        if (tamp.compareTo(currentTime) > 0) {
            String hash =  DigestUtils.md5DigestAsHex((KEY + "@" + tamp + "@" + code).getBytes());//生成MD5值
            if (hash.equalsIgnoreCase(requestHash)){
                //校验成功
                return "success";
            }else {
                //验证码不正确，校验失败
                return "failed";
            }
        } else {
            // 超时
            return "failed";
        }
    }


    //生成n位验证码
    private String VerifyCode(int n){
        Random r = new Random();
        StringBuffer sb =new StringBuffer();
        for(int i = 0;i < n;i ++){
            int ran1 = r.nextInt(10);
            sb.append(String.valueOf(ran1));
        }
//        System.out.println(sb);
        return sb.toString();
    }
    private void sendEmail(int type,String sendTo,String code){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //邮件设置值
        simpleMailMessage.setSubject("验证码-校园二手交易平台");//邮件主题
        if(type == 0){
            simpleMailMessage.setText("亲爱的用户：\n您正在注册成为本平台用户\n本次的邮箱验证码为："+code+"(仅15分钟内有效)\n如果这不是您的邮件，请忽略此邮件。\n" +
                    "校园二手交易平台系统邮件，请勿回复。");//邮件内容
        }else{
            simpleMailMessage.setText("亲爱的用户：\n您正在重置密码\n本次的邮箱验证码为："+code+"(仅15分钟内有效)\n如果这不是您的邮件，请忽略此邮件。\n" +
                    "校园二手交易平台平台系统邮件，请勿回复。");//邮件内容
        }
        simpleMailMessage.setTo(sendTo);//邮件发给谁
        simpleMailMessage.setFrom("黄于晗账号<1061919896@qq.com>"); //邮件来自于谁
        javaMailSender.send(simpleMailMessage);
    }

}

