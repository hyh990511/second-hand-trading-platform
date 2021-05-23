package com.shadouyou.backend.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.shadouyou.backend.JsonResult;
import com.shadouyou.backend.entity.*;
import com.shadouyou.backend.repository.*;
import com.shadouyou.backend.service.DataUtils;
import com.shadouyou.backend.service.Token.AdmiLoginToken;
import com.shadouyou.backend.service.Token.TokenService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admi")
public class AdministratorHandle {
    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public Object Login(@RequestBody Administrator administrator, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        String accountInput = administrator.getAccount();//vue 前端 输入的账号
        //将输入的密码转为md5
        String passwordInput = DigestUtils.md5DigestAsHex(administrator.getPassword().getBytes());
        Administrator administratorSearch = administratorRepository.findByAccount(accountInput);
        if (administratorSearch != null) {
            if (passwordInput.equals(administratorSearch.getPassword())){

                String token = tokenService.getToken(administratorSearch);
                String aid = administratorSearch.getAdministratorid().toString();
                jsonObject.put("token", token);
                jsonObject.put("message", aid);
//
//                Cookie cookie = new Cookie("token", token);
//                cookie.setPath("/");
//                response.addCookie(cookie);

                return jsonObject;
            }else {
                jsonObject.put("message", "Pw Error");
                return jsonObject;
            }

        } else {
            jsonObject.put("message", "Account does not exist");
            return jsonObject;
        }
    }

    @AdmiLoginToken
    @GetMapping("/info/{account}")
    public Administrator info (@PathVariable("account") String accountInput) {
        return administratorRepository.findByAccount(accountInput);
    }

    @AdmiLoginToken
    @PostMapping("/create")
    public String saveAdmi(@RequestBody Administrator administrator) {
        administrator.setPassword(DigestUtils.md5DigestAsHex(administrator.getPassword().getBytes()));
        Administrator result = administratorRepository.save(administrator);
        if (result != null) {
            return "success";
        } else {
            return "error";
        }
    }

    @AdmiLoginToken
    @PutMapping("/setting")
    public String updateAdmi(@RequestBody Administrator administrator) {
        Administrator result = administratorRepository.findByAdministratorid(administrator.getAdministratorid());
        result.setPassword(DigestUtils.md5DigestAsHex(administrator.getPassword().getBytes()));
        result.setNickname(administrator.getNickname());
        administratorRepository.save(result);
        if (result != null) {
            return "success";
        } else {
            return "error";
        }
    }

    @AdmiLoginToken
    @DeleteMapping("/delete/{id}")
    public void deleteAdmi(@PathVariable("id") Integer id) {
        administratorRepository.deleteById(id);
    }

    @GetMapping("/getLevel/{id}")
    public String getLevel(@PathVariable("id") Integer id) {
        Administrator administrator = administratorRepository.findById(id).get();
        String admiLevel = administrator.getAccesslevel().toString();

        return admiLevel;
    }

//    @GetMapping("/getInfoById/{id}")
//    public String getInfo(@PathVariable("id") Integer id)
//    {
//        Map<String,String> info=new HashMap<>();
//        Administrator admin=administratorRepository.findByAdministratorid(id);
//        info.put("nickname",admin.getNickname());
//        info.put("password",admin.getPassword());
//        return new Gson().toJson(info);
//
//    }

    @AdmiLoginToken
    @GetMapping("/getInfoById/{id}")
    public AdmiPage getInfo(@PathVariable("id") Integer id) {
        Administrator a = administratorRepository.findByAdministratorid(id);
        return new AdmiPage(a);
    }

    @AdmiLoginToken
    @GetMapping("/commentinfo/{id}")
    public Comment getcommentinfo(@PathVariable("id") Integer id)
    {
        Comment comment = commentRepository.findById(id).get();
        return  comment;
    }



    @AdmiLoginToken
    @GetMapping("/list/{page}/{size}")
    public Page<AdmiPage> findAdmi(@PathVariable("page") Integer page,
                                        @PathVariable("size") Integer size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        List<AdmiPage> admiPages = administratorRepository.pagefindAll();
        return DataUtils.listToPage(admiPages,pageRequest);

    }

    @AdmiLoginToken
    @GetMapping("/zeroExamGoods/list/{page}/{size}")
    public Page<Goods> findGoodsOfZeroExam(@PathVariable("page") Integer page,
                                           @PathVariable("size") Integer size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        return goodsRepository.findAllByIsexamine(0,pageRequest);//此处的0为未审核

    }

    @AdmiLoginToken
    @GetMapping("/goodinfo/{goodsid}")
    public Goods info (@PathVariable("goodsid") Integer gid){

        Goods goods = goodsRepository.findByGoodsid(gid);

        return goods;

    }

    @AdmiLoginToken
    @PutMapping("/GoodsExam/{id}")
    public String GoodsExam(@RequestBody Goods good,@PathVariable("id") Integer id) {
        Date dd = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sim.format(dd);


        Goods _good =goodsRepository.findByGoodsid(id);
//        _good.setIsexamine(1);//置1表示 审核过了
        _good.setExaminetime(time);//审核时间
        if (good.getIsexamine() == 1) {
            _good.setIsexamine(1);
        }
        else {
            _good.setIsexamine(2);
            //0：待审核、1：审核通过、2：不通过
            _good.setIssale(1);
            //商品在架状态，0：在架、1：下架、2：交易中
            _good.setNopassreason(good.getNopassreason());
            Message message = new Message();
            message.setReceiverid(_good.getUserid());
            message.setMessage(good.getNopassreason());
            message.setType(0);
            message.setIsread(0);
            messageRepository.save(message);
        }
//        _good.setAdministratorid(good.getAdministratorid());
//        if(good.getIspass()==1)
//        {
//            System.out.println("passed");
//            _good.setIspass(1);
//        }
//        else
//        {
//            System.out.println("not passed");
//            _good.setIspass(0);
//            _good.setNopassreason(good.getNopassreason());
//        }
        Goods result = goodsRepository.save(_good);
        if (result != null) {
            return "success";
        } else {
            return "error";
        }
    }

    @AdmiLoginToken
    @GetMapping("/list/{page}/{size}/{level}")
    public Page<AdmiPage> findAdmiLevel(@PathVariable("page") Integer page,
                                             @PathVariable("size") Integer size,
                                             @PathVariable("level") Integer level) {
        PageRequest pageRequest = PageRequest.of(page,size);
        List<AdmiPage> admiPageList = administratorRepository.pagefindAllByLevel(level);

        return DataUtils.listToPage(admiPageList,pageRequest);


    }

    @AdmiLoginToken
    @PutMapping("/banByid/{id}")
    public String banUser (@PathVariable("id") Integer id) {
        boolean flag = isIntheTrade(id);//是否有未完成交易
        if (flag) {
            return "error";
        } else {
            //清空商品、评论
            List<Goods> goodsList = goodsRepository.findAllByUserid(id);
            goodsRepository.deleteInBatch(goodsList);
            List<Comment> commentList = commentRepository.findAllByUserid(id);
            commentRepository.deleteInBatch(commentList);
            //封号
            User user = userRepository.findById(id).get();
            user.setIsuse(0);//0：封禁，1：正常
            User result = userRepository.save(user);
            return "success";
        }




    }

    @AdmiLoginToken
    @PutMapping("/reliveBanByid/{id}")
    public String reliveBan (@PathVariable("id") Integer id) {
        User user = userRepository.findById(id).get();
        user.setIsuse(1);//1为未禁用，此处设为1
        User result = userRepository.save(user);
        if (result != null) {
            return "success";
        } else {
            return "error";
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







}
