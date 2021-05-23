package com.shadouyou.backend.controllor;

import com.google.gson.internal.$Gson$Types;
import com.shadouyou.backend.controller.GoodsHandle;
import com.shadouyou.backend.controller.UserHandle;
import com.shadouyou.backend.entity.*;
import com.shadouyou.backend.repository.*;

import lombok.Generated;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@SpringBootTest
class UserHandleTest {
    @Autowired
    private UserHandle userHandle;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private GoodsHandle goodsHandle;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CommentRepository commentRepository;
//    @Test
//    void userfind(){
//        System.out.println(userHandle.findAll());
//    }

//    @Test
//    void userlogin(){
//        User user = userRepository.findById(1).get();
//        System.out.println(userHandle.userlogin(user));
//    }

//    @Test
//    void info(){
//        System.out.println(userHandle.info("221701207"));
//    }

//    @Test
//    void setUser(){
//        User user = userRepository.findById(1).get();
//        user.setNickname("py");
//        user.setPassword("b221701207");
//        System.out.println(userHandle.setUser(user));
//    }

//    @Test
//    void getOrder(){
//        System.out.println(userHandle.getOrder("221701207",0,1));
//    }

//    @Test
//    void deleteOrder(){
//        System.out.println(userHandle.deleteOrder(3));
//        int idInput = 99;
//        Order_ result = orderRepository.findByOrderid(idInput);
//        //System.out.println(result.getGoodsid());
//        Goods goods = goodsRepository.findByGoodsid(result.getGoodsid());
//        goods.setIssale(0);
//        goodsRepository.save(goods);
//    }

//    @Test
//    void confirmOrder(){
//        System.out.println(userHandle.confirmOrder(2));
//        System.out.println(userHandle.getOrder());
//    }

//    @Test
//    void commentOrder(){
//        System.out.println(userHandle.commentOrder(1,8));
//    }

//    @Test
//    void goodsSearch(){
//        System.out.println(userHandle.goodsSearch("ods"));
//    }

//      @Test
//      void goodsInof(){
//          System.out.println(userHandle.goodsinfo());
//      }

//        @Test
//        void goodsInof(){
//        System.out.println(userHandle.goodsinfo("g3"));
//        }

//          @Test
//          void goodsInof(){
//          System.out.println(userHandle.goodsinfo(2));
//          }

//            @Test
//            void buyGoods(){
//                System.out.println(goodsHandle.buyGoods("221701207",3));
//            }

//              @Test
//                void findtag(){
//                  System.out.println(tagRepository.findByAscriptionAndTagname("books","人文"));
//              }

//                @Test
//                void goodsbuy(){
//                    Order_ order_ = new Order_();
//                    order_.setGoodsid(1);
//                    order_.setSendway(0);
//                    System.out.println(goodsHandle.buyGoods(order_,"221701207"));
//                }

//                     @Test
//                void goodsbuy2(){
//                    Order_ order_ = new Order_();
//                    order_.setGoodsid(2);
//                    order_.setUserid(2);
//                    order_.setSendway(0);
//                    System.out.println(goodsHandle.buyGoods(order_));
//                }

//            @Test
//            void findgooduser()
//            {
//                GoodsUser goodsUser = goodsRepository.findAllGoodsUserById(1);
//                System.out.println(goodsUser.getgoodsID());
//                System.out.println(goodsUser.gettag());
//                System.out.println(goodsUser.getchildtag());
//                System.out.println(goodsUser.getascription());
//                System.out.println(goodsUser.getcreditRating());
//                System.out.println(goodsUser.getnickname());
//            }
//
//            @Test
//            void findgoodusers()
//            {
//                Integer page = 1;
//                Integer size = 1;
//                Pageable pageable = PageRequest.of(page-1,size);
//                List<GoodsUser> goodsUser = goodsRepository.findAllGoodsUserByName("g3","common");
//                Page<GoodsUser> goodsUsers = new PageImpl<>(goodsUser,pageable,goodsUser.size());
//                for (GoodsUser goodsUser1: goodsUsers
//                     ) {
//                    System.out.println(goodsUser1.getgoodsID());
//                    System.out.println(goodsUser1.gettag());
//                    System.out.println(goodsUser1.getchildtag());
//                    System.out.println(goodsUser1.getascription());
//                    System.out.println(goodsUser1.getcreditRating());
//                    System.out.println(goodsUser1.getnickname());
//                }
//
//            }

//            @Test
//            void info(){
//                GoodsUser goodsUser = goodsHandle.info(1);
//                System.out.println(goodsUser.getgoodsID());
//                    System.out.println(goodsUser.gettag());
//                    System.out.println(goodsUser.getchildtag());
//                    System.out.println(goodsUser.getascription());
//                    System.out.println(goodsUser.getcreditRating());
//                    System.out.println(goodsUser.getnickname());
//            }

//        @Test
//        void issueGoods(){
//            Goods goods = goodsRepository.findByGoodsid(1);
//            goods.setGoodsid(7);
//            goodsHandle.issueGoods(goods,"221701207");
//        }

//        @Test
//        void getSaleGoods(){
//            System.out.println(goodsHandle.getSaleGoods(2,"common"));
//        }


//        @Test
//        void ordercancel()
//        {
//            userHandle.ordercancel(5);
//        }

//        @Test
//        void orderappeal()
//        {
//            Appeal appeal = new Appeal();
//            appeal.setOrderid(4);
//            appeal.setAppealreason("垃圾还卖那么贵");
//            userHandle.orderappeal(appeal);
//        }


//        @Test
//        void findCommentUserByGoodsid()
//        {
//          Page<CommentUser> commentUser = userHandle.getcommentinfo(1,1,1);
//            System.out.println(commentUser);
//           for(CommentUser cu : commentUser )
//           {
//               System.out.println(cu.getgoodsID());
//               System.out.println(cu.getreleaseTime());
//               System.out.println(cu.getgoodsID());
//               System.out.println(cu.gettext());
//               System.out.println(cu.getnickname());
//           }
//}

//        @Test
//        void delivercomment()
//        {
//            Comment comment = new Comment();
//            comment.setGoodsid(2);
//            comment.setText("有问题不");
//            String accountnumber = "221701212";
//            userHandle.delivercomment(comment,accountnumber);
//        }


//         @Test
//         void replycomment()
//         {
//             Comment comment = new Comment();
//             comment.setGoodsid(1);
//             comment.setText("你好啊");
//             String accountnumber = "221701207";
//             userHandle.replycomment(comment,accountnumber,2);
//         }


//            @Test
//            void inform()
//            {
//                Comment comment = new Comment();
//                comment.setCommentid(1);
//                comment.setText("举报一波走起");
//                String accountnumber = "221701212";
//                userHandle.inform(comment,accountnumber);
//            }

//            @Test
//            void getcommentinfo()
//            {
//                System.out.println(userHandle.getcommentinfo(1));
//            }
}