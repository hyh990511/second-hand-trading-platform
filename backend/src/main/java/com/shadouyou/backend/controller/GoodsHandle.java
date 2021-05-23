package com.shadouyou.backend.controller;

import com.shadouyou.backend.entity.*;
import com.shadouyou.backend.repository.*;
import com.shadouyou.backend.service.BaiduAI.CheckTextAPI;
import com.shadouyou.backend.service.GoodsService;
import com.shadouyou.backend.service.Token.PassToken;
import com.shadouyou.backend.service.Token.UserLoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/goods")
public class GoodsHandle {
    CheckTextAPI checkApi = new CheckTextAPI();

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TagRepository tagRepository;

    @UserLoginToken
    @GetMapping("/findAll/{page}/{size}")
    public Page<Goods> findAll (@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        PageRequest pageRequest = PageRequest.of(page,size);
//        return goodsRepository.findAllByIsexamineAndIspassAndIssale(1,1,0,pageRequest);
        return goodsRepository.findAllByIsexamineAndIssale(1,0,pageRequest);
    }
    @UserLoginToken
    @GetMapping("/findAll")
    public List<Goods> findAll () {

//        return goodsRepository.findAllByIsexamineAndIspassAndIssale(1,1,0);
        return goodsRepository.findAllByIsexamineAndIssale(1,1);
    }

    //以下为获取商品详细信息功能
    @UserLoginToken
    @GetMapping("/info/{goodsid}")
    public GoodsUser info (@PathVariable("goodsid") Integer gid){

        Goods goods = goodsRepository.findByGoodsid(gid);
        Integer id = goods.getUserid();
//        String ascription = goods.getAscription();
//        String tag;
//        if(!ascription.equals("books"))
//            tag = goods.getTag();
//        else
//            tag = goods.getChildtag();
//
//        Tag tag1 = tagRepository.findByAscriptionAndTagname(ascription,tag);
//        tag1.setHot(tag1.getHot()+1);
//        tagRepository.save(tag1);

    return goodsRepository.findAllGoodsUserById(gid);

    }

    //以下为获取依据关键词搜索的商品列表功能
    @UserLoginToken
    @GetMapping("/search/{page}/{size}/{key}/{ascription}")
    public Page<GoodsUser> search(@PathVariable("page") Integer page,@PathVariable("size") Integer ssize,@PathVariable("key") String key,@PathVariable("ascription") String ascription){
        int size = ssize;
        Pageable pageable = PageRequest.of(page-1,size);
//        return goodsRepository.findByNameContainingAndIspassAndIsexamineAndIssaleOrderByRealasetime(key,1,1,0,pageable);
        List<GoodsUser> goodsUser = goodsRepository.findAllGoodsUserByName(key,ascription);
        Page<GoodsUser> goodsUsers = new PageImpl<GoodsUser>(goodsUser,pageable,goodsUser.size());
        return  goodsUsers;
    }

    //以下为发布商品功能
    @UserLoginToken
    @PostMapping("/issue/{account}")
    public void issueGoods(@RequestBody Goods goods,@PathVariable("account") String account)  {
        User u=userRepository.findUserByAccountnumber(account);
//        goods.setUsername(u.getNickname());
        goods.setUserid(u.getUserid());
//        goods.setAdministratorid(0);
//        goods.setIspass(0);
        //敏感词检测
        String namecheck = checkApi.check(goods.getName());
        String titlecheck = checkApi.check(goods.getTitle());
        String textcheck = checkApi.check(goods.getText());
        if (namecheck.equals("合规") && titlecheck.equals("合规") && textcheck.equals("合规")) {
            //敏感词检测通过 直接设为审核通过
            Date dd = new Date();
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sim.format(dd);
            goods.setExaminetime(time);
            goods.setIsexamine(1);
            goods.setIssale(0);
        } else {
            //敏感词审核不通过，需要人工审核
            goods.setIsexamine(0);
            goods.setIssale(0);
        }


        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String releasetime = sdf.format(date);
        goods.setRealasetime(releasetime);
        goodsRepository.save(goods);
        //return  "redirect:/";
    }

    //以下为购买商品功能
    @UserLoginToken
    @PostMapping("/buy/{account}")
    public String buyGoods(@RequestBody Order_ oorder_,@PathVariable("account") String account) {

        User user1 = userRepository.findUserByAccountnumber(account);
        Integer user1_id = user1.getUserid();
        Integer user2_id = goodsRepository.findByGoodsid(oorder_.getGoodsid()).getUserid();
        if(user1_id==user2_id) return "failed";

        String useraccount = account;
        Integer gid = oorder_.getGoodsid();
        Order_ order_ = new Order_();
        Goods goods = goodsRepository.findByGoodsid(gid);
        boolean success = false;
        User user = userRepository.findUserByAccountnumber(useraccount);
//        && goods.getIspass() != 0
        if(goods.getIssale() == 0 ) {
            order_.setGoodsid(gid);
            goods.setIssale(2);
            order_.setGoodsprice(goods.getPrice());
            order_.setGoodsname(goods.getName());
            order_.setSendway(oorder_.getSendway());
            order_.setUserid(user.getUserid());
            order_.setTradingstatus("待收货");
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String releasetime = sdf.format(date);
            order_.setReleasetime(releasetime);
            Calendar calendar2 = Calendar.getInstance();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            calendar2.add(Calendar.DATE, 3);
            String endtime = sdf2.format(calendar2.getTime());
            order_.setEndtime(endtime);
            order_.setIscomment(0);
            order_.setCreditrating(user.getCreditrating());
            order_.setImgfirst(goods.getImgfirst());
            order_.setSendway(oorder_.getSendway());
            if(oorder_.getSendway()!=0)
            {
                Double sendprice = goods.getSendprice();
                order_.setSendprice(sendprice);
            }
            else
            {
                order_.setSendprice(0.0);
            }
            Order_ result1 = orderRepository.save(order_);
            Goods result2 = goodsRepository.save(goods);

            if (result1 != null && result2 != null) {
                success = true;
            } else {
                success = false;
            }
        }
        if(success){

            Message message = new Message();
            String phonenumber = "";
            if (user.getPhone() == null) {
                phonenumber = "买家未填写联系方式";

            } else {
                phonenumber = user.getPhone();
            }
            message.setMessage("您发布的"+goods.getName()+"商品已被"+user.getNickname()+"购买，联系方式（电话）："+phonenumber);
            message.setIsread(0);
            message.setType(0);
            message.setReceiverid(goods.getUserid());
            messageRepository.save(message);

            String ascription = goods.getAscription();
            String tag;
            if(!ascription.equals("books"))
                tag = goods.getTag();
            else
                tag = goods.getChildtag();

            Tag tag1 = tagRepository.findByAscriptionAndTagname(ascription,tag);
            tag1.setHot(tag1.getHot()+1);
            tagRepository.save(tag1);

            return "success";

        }else{
            return "error";
        }
    }


    //以下为删除已发布商品功能
    @UserLoginToken
    @DeleteMapping("/delete/{goodsid}")
    public void deleteGoods(@PathVariable("goodsid") Integer goodsid){
        goodsService.deleteGoodsById(goodsid);
       // return "redirect:/";
    }

    @UserLoginToken
    @DeleteMapping("/delete")
    public void deleteGoods(Goods goods){
        goodsRepository.delete(goods);
        // return "redirect:/";
    }

    //以下为获取在售商品信息
    @UserLoginToken
    @PostMapping("/init/{size}/{ascription}")
    public List<GoodsUser> getSaleGoods(@PathVariable("size") Integer size,@PathVariable("ascription") String ascription)
    {
        List<GoodsUser> goods = goodsRepository.findAllGoodsByAscription(size,ascription);
        return  goods;
    }

    //分页获取用户已发布商品列表
    @UserLoginToken
    @GetMapping("/issue/list/{accountNumber}/{page}/{size}")
    public Page<Goods> listOfGoods (@PathVariable("accountNumber") String accountNumber,
                                    @PathVariable("page")  Integer page,
                                    @PathVariable("size")  Integer size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        //首先获取账号对应的用户id
        User user = userRepository.findUserByAccountnumber(accountNumber);
        Integer uid = user.getUserid();

        return goodsRepository.findAllByUserid(uid,pageRequest);
    }

    //下架已发布的商品
    @UserLoginToken
    @PutMapping("/issue/cancel/{gid}")
    public void cancelGoods(@PathVariable("gid") Integer id) {
        Goods goods = goodsRepository.findById(id).get();
        //检查状态issale     0：在架、1：下架、2：交易中
        Integer issale = goods.getIssale();
        if (issale == 0) {
            goods.setIssale(1);
        }
        goodsRepository.save(goods);

    }

    //修改已发布的商品
    @UserLoginToken
    @PutMapping("/issue/alter")
    public void alterGoods(@RequestBody Goods goods) {
        Goods _good = goodsRepository.findById(goods.getGoodsid()).get();
        _good.setGoodsid(goods.getGoodsid());
        _good.setName(goods.getName());
        _good.setTitle(goods.getTitle());
        _good.setText(goods.getText());
        _good.setGoodsprice(goods.getGoodsprice());
        _good.setSendprice(goods.getSendprice());
        _good.setImgfirst(goods.getImgfirst());
        _good.setImgsecond(goods.getImgsecond());
        _good.setImgthird(goods.getImgthird());
        _good.setExchangebook(goods.getExchangebook());
        goodsRepository.save(_good);
    }

    @UserLoginToken
    @GetMapping("/hot/{ascription}/{size}")
    public List<GoodsUser> hotAsGoods(@PathVariable("ascription") String ascription,
                                  @PathVariable("size") Integer size) {
        if (ascription.equals("books")) {
            //以书易书区
            List<Tag> bookstaglist = tagRepository.findAllByAscriptionOrderByHotDesc("books");
            Integer i = 0;
            Tag tag = bookstaglist.get(i);
            String tN = tag.getTagname();
            List<GoodsUser> goodsList = goodsRepository.findAllByChildtagAndIssaleAndIsexamine(tN,ascription);
            while (goodsList.size() == 0) {
                if (i < bookstaglist.size() -1) {
                    i++;
                } else {
                    break;
                }
                tag = bookstaglist.get(i);
                tN = tag.getTagname();
                goodsList = goodsRepository.findAllByChildtagAndIssaleAndIsexamine(tN,ascription);
            }
            Integer glSize = goodsList.size();
            if (glSize >= size) {
                List<GoodsUser> newgoodsList = goodsList.subList(0,size);
                return  newgoodsList;
            } else {
                return goodsList;
            }

        } else {
            List<Tag> taglist = tagRepository.findAllByAscriptionOrderByHotDesc(ascription);
            Integer i = 0;
            Tag tag = taglist.get(i);
            String tN = tag.getTagname();
            List<GoodsUser> goodsList = goodsRepository.findAllByTagAndIssaleAndAscriptionAndIsexamine(tN,ascription);
            while (goodsList.size() == 0) {
                if (i < taglist.size() -1) {
                    i++;
                } else {
                    break;
                }
                 tag = taglist.get(i);
                 tN = tag.getTagname();

                goodsList = goodsRepository.findAllByTagAndIssaleAndAscriptionAndIsexamine(tN,ascription);
            }
            Integer glSize = goodsList.size();
            if (glSize >= size) {
                List<GoodsUser> newgoodsList = goodsList.subList(0,size);
                return  newgoodsList;
            } else {
                return goodsList;
            }
        }

    }

}