package com.shadouyou.backend.DyTest;

import com.shadouyou.backend.controller.InformHandle;
import com.shadouyou.backend.entity.*;
import com.shadouyou.backend.repository.*;
import com.shadouyou.backend.service.BaiduAI.CheckTextAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.regex.Pattern;

import java.util.List;

@SpringBootTest
public class DyTest {
    @Autowired
    private AppealRepository appealRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private InformRepository informRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    // /complain/list/{page}/{pagesize}
    // 分页获取未处理申诉列表
    @Test
    void findAppeal() {
        PageRequest pageRequest1 = PageRequest.of(0,2);
        PageRequest pageRequest2 = PageRequest.of(1,2);
        PageRequest pageRequest3 = PageRequest.of(0,10);

        List<Appeal> list1 = appealRepository.findAllByState(0,pageRequest1).getContent();
        List<Appeal> list2 = appealRepository.findAllByState(0,pageRequest2).getContent();
        List<Appeal> list3 = appealRepository.findAllByState(0,pageRequest3).getContent();

        System.out.println(list1);
        System.out.println(list2);
        System.out.println(list3);
    }


    // /complain/disposing/
    // 处理申诉，修改申诉表状态，并向申诉人发送通知：“申诉已受理，我们将派专人与您联系”，类型为系统通知
    @Test
    void disposingAppeal() {
        Appeal appeal = appealRepository.findById(5).get();
        Integer uid = appeal.getUserid();
        appeal.setState(1);//处理中
        String oid = appeal.getOrderid().toString();

        Message message = new Message();
        message.setReceiverid(uid);
        message.setMessage("订单编号"+oid+"的申诉已受理，我们将派专人与您联系");
        message.setType(0);//系统通知
        message.setIsread(0);//未读

        messageRepository.save(message);
        appealRepository.save(appeal);
    }

    // /complain/disposed/{aid}
    // 订单处理完毕，管理员发通知给用户
    @Test
    void disposedAppeal() {
        Message message = new Message();
        message.setMessage("测试订单处理完毕");
        Appeal appeal = appealRepository.findById(5).get();

        message.setReceiverid(appeal.getUserid());
        message.setType(0);
        message.setIsread(0);
        messageRepository.save(message);

        appeal.setState(2);//处理完毕
        appealRepository.save(appeal);

    }

    // /inform/legal/{iid}
    // 举报处理——未违规，直接修改处理状态
    @Test
    void legalInform() {
        Inform inform = informRepository.findById(1).get();
        inform.setState(1);
        informRepository.save(inform);

    }

    @Test
    public void banUser() {
        Integer id = 8;
        //首先根据举报表id：iid 查找被举报对象属于商品还是评论
        Inform inform = informRepository.findById(id).get();
        Integer type = inform.getType();
        Integer oid = inform.getObjectid();//对象id
        //判断类型属于商品还是评论
        if (type == 0) {
            //0：商品，1：评论
            Goods goods = goodsRepository.findById(oid).get();
            Integer uid = goods.getUserid();//获取对应商品的发布者id
            boolean flag = isIntheTrade(uid);//是否有未完成交易
            if (flag) {
                System.out.println("failed");
            } else {
                //清空商品、评论
                List<Goods> goodsList = goodsRepository.findAllByUserid(uid);
                goodsRepository.deleteInBatch(goodsList);
                List<Comment> commentList = commentRepository.findAllByUserid(uid);
                commentRepository.deleteInBatch(commentList);
                //封号
                User user = userRepository.findById(uid).get();
                user.setIsuse(0);//0：封禁，1：正常
                userRepository.save(user);
                System.out.println("success");
            }

        } else {
            //0：商品，1：评论
            Comment comment = commentRepository.findById(oid).get();
            Integer uid = comment.getUserid();//获取对应评论的评论者id
            boolean flag = isIntheTrade(uid);//是否有未完成交易
            if (flag) {
                System.out.println("failed");
            } else {
                //清空商品、评论
                List<Goods> goodsList = goodsRepository.findAllByUserid(uid);
                goodsRepository.deleteInBatch(goodsList);
                List<Comment> commentList = commentRepository.findAllByUserid(uid);
                commentRepository.deleteInBatch(commentList);
                //封号
                User user = userRepository.findById(uid).get();
                user.setIsuse(0);//0：封禁，1：正常
                userRepository.save(user);
                System.out.println("success");
            }
        }

    }

    public boolean isIntheTrade(Integer uid) {
        //计数：商品表中userid为uid 且 issale 为2(交易中)
        Integer count = goodsRepository.countByUseridAndIssale(uid,2);
        if (count > 0) {
            //存在交易中的商品
            return true;
        } else {
            //不存在交易中的商品
            return false;
        }
    }

    @Test
    public void alter() {
        Goods _good = goodsRepository.findById(1).get();
        _good.setGoodsid(1);
        _good.setName("11");
        _good.setTitle("goods.getTitle()");
        _good.setText("goods.getText()");
        _good.setGoodsprice(0.0);
        _good.setSendprice(0.0);
        _good.setImgfirst("goods.getImgfirst()");
        _good.setImgsecond("goods.getImgsecond()");
        _good.setImgthird("goods.getImgthird()");
        goodsRepository.save(_good);
    }

    //屏蔽
    @Test
    public void xx(){
        User user = userRepository.findUserByAccountnumber("221701207");
        Integer uid = user.getUserid();
        List<Message> messageList = messageRepository.findAllByReceiverid(uid);
        messageRepository.deleteInBatch(messageList);
        System.out.println("ok");
    }

    //md5
    //md5
    @Test
    public void  md5Test(){
        String i1 = "a221701207";
        String m1 = DigestUtils.md5DigestAsHex(i1.getBytes());
        System.out.println(i1+"--》"+m1);
        String i2 = "a221701212";
        String m2 = DigestUtils.md5DigestAsHex(i2.getBytes());
        System.out.println(i2+"--》"+m2);
        String i3 = "a221701218";
        String m3 = DigestUtils.md5DigestAsHex(i3.getBytes());
        System.out.println(i3+"--》"+m3);
        String i4 = "a221701226";
        String m4 = DigestUtils.md5DigestAsHex(i4.getBytes());
        System.out.println(i4+"--》"+m4);
        String i5 = "a221701229";
        String m5 = DigestUtils.md5DigestAsHex(i5.getBytes());
        System.out.println(i5+"--》"+m5);
        String i6 = "a221701235";
        String m6 = DigestUtils.md5DigestAsHex(i6.getBytes());
        System.out.println(i6+"--》"+m6);
        String i7 = "a111111111";
        String m7 = DigestUtils.md5DigestAsHex(i7.getBytes());
        System.out.println(i7+"--》"+m7);

    }

}
