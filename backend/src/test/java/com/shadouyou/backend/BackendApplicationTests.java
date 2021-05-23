package com.shadouyou.backend;

import com.shadouyou.backend.entity.User;
import com.shadouyou.backend.repository.GoodsRepository;
import com.shadouyou.backend.repository.NoticeRepository;
import com.shadouyou.backend.repository.OrderRepository;
import com.shadouyou.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.SimpleTimeZone;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserRepository userRepository;

    @Test
    void xx() {
        System.out.println(orderRepository.findAll());


    }
    @Test
    void contextLoads() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //邮件设置值
        simpleMailMessage.setSubject("xx同学");//邮件主题
        simpleMailMessage.setText("您的验证码是：1234");//邮件内容
        simpleMailMessage.setTo("357528492@qq.com");//邮件发给谁
        simpleMailMessage.setFrom("黄于晗账号<1061919896@qq.com>"); //邮件来自于谁
//        javaMailSender.send(simpleMailMessage);
        Random r = new Random();
        StringBuffer sb =new StringBuffer();
        for(int i = 0;i < 6;i ++){
            int ran1 = r.nextInt(10);
            sb.append(String.valueOf(ran1));
        }
        System.out.println(sb);
//        User user = userRepository.findUserByAccountnumber("221701207");
//        System.out.println(user);
    }


}
