package com.shadouyou.backend.controller;

import com.shadouyou.backend.entity.*;
import com.shadouyou.backend.repository.*;
import com.shadouyou.backend.service.Token.AdmiLoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inform")
public class InformHandle {
    @Autowired
    private InformRepository informRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    @AdmiLoginToken
    @GetMapping("/list/{type}/{page}/{size}/{state}")
    public Page<Inform> findInform(@PathVariable("page")  Integer page,
                                   @PathVariable("size")  Integer size,
                                   @PathVariable("type")  Integer type,
                                   @PathVariable("state") Integer state) {
        PageRequest pageRequest = PageRequest.of(page,size);
        return informRepository.findAllByTypeAndState(type,state,pageRequest);

    }

    @AdmiLoginToken
    @PutMapping("/legal/{iid}")
    public void legalInform(@PathVariable("iid") Integer id) {
        Inform inform = informRepository.findById(id).get();
        inform.setState(1);
        informRepository.save(inform);
    }

    @AdmiLoginToken
    @PutMapping("/banUser/{iid}")
    public String banUser(@PathVariable("iid") Integer id) {
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
                return "failed";
            } else {
                //封号
                banUserMethod(uid);
                //修改处理状态
                inform.setState(1);
                informRepository.save(inform);
                return "success";
            }

        } else {
            //0：商品，1：评论
            Comment comment = commentRepository.findById(oid).get();
            Integer uid = comment.getUserid();//获取对应评论的评论者id
            boolean flag = isIntheTrade(uid);//是否有未完成交易
            if (flag) {
                return "failed";
            } else {
                //封号
                banUserMethod(uid);
                //修改处理状态
                inform.setState(1);
                informRepository.save(inform);
                return "success";
            }
        }

    }

    @AdmiLoginToken
    @PutMapping("/deduct/{iid}")
    public String deductInform(@RequestBody Message message,
                               @PathVariable("iid") Integer id) {
        //首先根据举报表id：iid 查找被举报对象属于商品还是评论
        Inform inform = informRepository.findById(id).get();
        Integer type = inform.getType();
        Integer oid = inform.getObjectid();//对象id
        //判断类型属于商品还是评论
        if (type == 0) {
            //0：商品，1：评论
            Goods goods = goodsRepository.findById(oid).get();
            Integer uid = goods.getUserid();//获取对应商品的发布者id
            User user = userRepository.findById(uid).get();
            Double creditRating = user.getCreditrating();
            if (creditRating >= 1) {
                //扣分
                Double creditChange = creditRating-1.0;
                user.setCreditrating(creditChange);
                goods.setIssale(1);
                goodsRepository.save(goods);
                //修改处理状态
                inform.setState(1);
                informRepository.save(inform);
                //消息
                message.setReceiverid(uid);
                message.setType(0);
                message.setIsread(0);
                messageRepository.save(message);
                return "success";
            } else {
                boolean flag = isIntheTrade(uid);//是否有未完成交易
                if (flag) {
                    return "failed";
                } else {
                    //封号
                    banUserMethod(uid);
                    //修改处理状态
                    inform.setState(1);
                    informRepository.save(inform);
                    return "success";
                }
            }

        } else {
            //0：商品，1：评论
            Comment comment = commentRepository.findById(oid).get();
            Integer uid = comment.getUserid();//获取对应评论的评论者id
            User user = userRepository.findById(uid).get();
            Double creditRating = user.getCreditrating();
            if (creditRating >= 1) {
                //扣分
                Double creditChange = creditRating-1;
                user.setCreditrating(creditChange);
                //删除相应评论
                commentRepository.delete(comment);
                //修改处理状态
                inform.setState(1);
                informRepository.save(inform);
                //消息
                message.setReceiverid(uid);
                message.setType(0);
                message.setIsread(0);
                messageRepository.save(message);
                return "success";
            } else {
                boolean flag = isIntheTrade(uid);//是否有未完成交易
                if (flag) {
                    return "failed";
                } else {
                    //封号
                    banUserMethod(uid);
                    //修改处理状态
                    inform.setState(1);
                    informRepository.save(inform);
                    return "success";
                }
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

    public void banUserMethod(Integer uid) {
        //清空商品、评论
        List<Goods> goodsList = goodsRepository.findAllByUserid(uid);
        goodsRepository.deleteInBatch(goodsList);
        List<Comment> commentList = commentRepository.findAllByUserid(uid);
        commentRepository.deleteInBatch(commentList);
        //封号
        User user = userRepository.findById(uid).get();
        user.setIsuse(0);//0：封禁，1：正常
        userRepository.save(user);
    }
}
