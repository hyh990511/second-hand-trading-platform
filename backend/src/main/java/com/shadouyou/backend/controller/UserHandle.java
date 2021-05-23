package com.shadouyou.backend.controller;

import com.shadouyou.backend.entity.*;
import com.shadouyou.backend.repository.*;
import com.shadouyou.backend.service.BaiduAI.CheckTextAPI;
import com.shadouyou.backend.service.DataUtils;
import com.shadouyou.backend.service.Token.AdmiLoginToken;
import com.shadouyou.backend.service.Token.PassToken;
import com.shadouyou.backend.service.Token.TokenService;
import com.shadouyou.backend.service.Token.UserLoginToken;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserHandle {
    private static final String KEY = "sha1dou2you3"; // KEY为自定义秘钥
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AppealRepository appealRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private InformRepository informRepository;
    @Autowired
    private TokenService tokenService;

    CheckTextAPI checkApi = new CheckTextAPI();

    @GetMapping("/findAll")
    public List<User> findAll () {
        return userRepository.findAll();
    }

    @AdmiLoginToken
    @GetMapping("/findAll/{page}/{size}")
    public Page<UserPage>findAll (@PathVariable("page") Integer page,@PathVariable("size") Integer size) {
        PageRequest pageRequest = PageRequest.of(page,size);

        List<UserPage> userPageList = userRepository.pagefindAll();


        return DataUtils.listToPage(userPageList,pageRequest);
    }

    @PostMapping("/login")
    public Object  userlogin(@RequestBody User user, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        String accountnumber = user.getAccountnumber();
        String passwordInput = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        User user1 = userRepository.findUserByAccountnumber(accountnumber);

        if (user1 != null) {
            if (passwordInput.equals(user1.getPassword()) && user1.getIsuse() != 0){
                String token = tokenService.getToken(user1);
                jsonObject.put("token", token);
                jsonObject.put("message", "Login Success");
                Cookie cookie = new Cookie("token", token);
                cookie.setPath("/");
                response.addCookie(cookie);

                return jsonObject;
            } else if (user1.getIsuse() == 0){
                jsonObject.put("message","The user has been banned");
                return jsonObject;
            } else {
                jsonObject.put("message", "Pw Error");
                return jsonObject;
            }

        } else {
            jsonObject.put("message", "Account does not exist");
            return jsonObject;

        }
    }

    @UserLoginToken
    @GetMapping("/info/{accountNumber}")
    public UserInfo info(@PathVariable("accountNumber") String accountNumber) {
        User user = userRepository.findUserByAccountnumber(accountNumber);
        UserInfo userInfo = new UserInfo(user);
        return userInfo;
    }
    @UserLoginToken
    @GetMapping("/other/info/{userid}")
    public UserInfo2 info(@PathVariable("userid") Integer userid) {
        User user = userRepository.findByUserid(userid);
        UserInfo2 userInfo2 = new UserInfo2(user);
        return userInfo2;
    }

//    @GetMapping("/info/{accountNumber}")
//    public User info(@PathVariable("accountNumber") String accountNumber) {
//        return userRepository.findUserByAccountnumber(accountNumber);
//    }

    @AdmiLoginToken
    @PutMapping("/setting")
    public String setUser(@RequestBody User user) {
        User u=userRepository.findByUserid(user.getUserid());
        //这里只改密码跟用户名
        System.out.println("nickname:"+user.getNickname()+" pwd "+user.getPassword());
        u.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        u.setNickname(user.getNickname());
        User result = userRepository.save(u);
        if (result != null) {
            return "success";
        } else {
            return "error";
        }
    }

    @UserLoginToken
    @PutMapping("/setting1")
    public String setUser1(@RequestBody User user) {
        User u=userRepository.findUserByAccountnumber(user.getAccountnumber());

        if(user.getNickname()!=null)
            u.setNickname(user.getNickname());
        if(user.getRealname()!=null)
            u.setRealname(user.getRealname());
        if(user.getPhone()!=null)
            u.setPhone(user.getPhone());
        if(user.getEmail()!=null)
            u.setEmail(user.getEmail());
        if(user.getQqnumber()!=null)
            u.setQqnumber(user.getQqnumber());
        if(user.getAddress()!=null)
            u.setAddress(user.getAddress());
        if(user.getIntroduction()!=null)
            u.setIntroduction(user.getIntroduction());
        if(user.getGender()!=null)
            u.setGender(user.getGender());
        if(user.getImages()!=null)
            u.setImages(user.getImages());

        User result = userRepository.save(u);
        if (result != null) {
            return "success";
        } else {
            return "error";
        }
    }

    @UserLoginToken
    @PutMapping("/settingPw/{oldpw}")
    public String setPw(@PathVariable("oldpw") String oldpw,@RequestBody User user){
        User u=userRepository.findUserByAccountnumber(user.getAccountnumber());
        oldpw = DigestUtils.md5DigestAsHex(oldpw.getBytes());
        if (!oldpw.equals(u.getPassword())) return "error";
        else {
            u.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            User result = userRepository.save(u);
            if (result != null){
                return "success";
            }
            else return "error";
        }
    }

    @PassToken
    @PutMapping("/resetPwd/{code}/{hash}/{time}")
    public String resetPw(@RequestBody User user,@PathVariable("code") String code,@PathVariable("hash") String requestHash,@PathVariable("time") String tamp){
        String hash =  DigestUtils.md5DigestAsHex((KEY + "@" + tamp + "@" + code).getBytes());//生成MD5值
        if (hash.equalsIgnoreCase(requestHash)){
            try {
                User u = userRepository.findUserByAccountnumber(user.getAccountnumber());
                u.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
                userRepository.save(u);
            }
            catch (Exception e)
            {
                return "failed";
            }
            finally {
                return "success";
            }
        }else {
            //验证码不正确，校验失败
            return "illegal";
        }
    }

    @UserLoginToken
    @GetMapping("/order/{accountNumber}")
    public List<Order_> getOrder(@PathVariable("accountNumber") String accountNumber) {
        User user = userRepository.findUserByAccountnumber(accountNumber);
        return  orderRepository.findAllByUserid(user.getUserid());
    }

    @UserLoginToken
    @GetMapping("/order/{accountNumber}/{page}/{size}")
    public Page<Order_> getOrder(@PathVariable("accountNumber") String accountNumber,
                                 @PathVariable("page") Integer page,@PathVariable("size") Integer size) {
        User user = userRepository.findUserByAccountnumber(accountNumber);
        PageRequest pageRequest = PageRequest.of(page,size, Sort.by("releasetime").descending());
        return  orderRepository.findAllByUserid(user.getUserid(),pageRequest);
    }

    @UserLoginToken
    @PostMapping("/order/affirm/{id}/{score}")
    public String confirmOrder(@PathVariable("id") Integer idInput,@PathVariable("score") Double score) {
        Order_ order_ = orderRepository.findById(idInput).get();
        order_.setTradingstatus("已收货");
        order_.setCreditrating(score);
        Integer gid = order_.getGoodsid();
        Goods goods = goodsRepository.findByGoodsid(gid);
        goods.setIssale(1);
        Integer uid = goodsRepository.findByGoodsid(gid).getUserid();
        User user = userRepository.findByUserid(uid);
        Double old_credit = user.getCreditrating();
        Double user_credit = userRepository.findByUserid(order_.getUserid()).getCreditrating();
        Double new_credit = old_credit+((score-old_credit)/5.0)*user_credit/3.0;
        user.setCreditrating(new_credit);
        userRepository.save(user);
        goodsRepository.save(goods);
        Order_ result = orderRepository.save(order_);
        if(result != null)
        {
            return "success";
        } else {
            return "error";
        }
    }

//    新的信用=旧信用+( (评价 - 旧信用)/5 )*买家信用/3，保留一位小数


//    @DeleteMapping("/order/cancel/{id}")
//    public String deleteOrder(@PathVariable("id") Integer idInput){
//        Order_ order = orderRepository.findByOrderid(idInput);
//        orderRepository.deleteById(idInput);
//        Order_ result = orderRepository.findByOrderid(idInput);
//        //商品重新上架
//        Goods goods = goodsRepository.findByGoodsid(order.getGoodsid());
//        goods.setIssale(0);
//        goodsRepository.save(goods);
//        //消息通知未完善
//        if( result == null){
//            return "success";
//        }
//        else
//        {
//            return "error";
//        }
//    }


    @UserLoginToken
    @PutMapping("/order/comment/{id}/{cd}")
    public String commentOrder(@PathVariable("id") Integer idInput, @PathVariable("cd") Double ctInput){

        Order_ order_ = orderRepository.findByOrderid(idInput);

        order_.setIscomment(1);
        order_.setCreditrating(ctInput);

        Order_ result = orderRepository.save(order_);

        if(order_.getIscomment()!=0){
            return "success";
        }
        else {
            return  "failed";
        }

    }

//    @PutMapping("/order/complain/{id}/{complain}")
//    public  String complainOrder(@PathVariable("id") Integer idInput,@PathVariable() String complain){
//        Order_ order_ = orderRepository.findByOrderid(idInput);
//
//        if(order_!=null){
//            return "success";
//        }
//        else {
//            return  "failed";
//        }
//    }

    @UserLoginToken
    @GetMapping("/goods/search/{name}")
    public List<Goods> goodsSearch(@PathVariable("name") String name){
        List<Goods> search = goodsRepository.findByName(name);
        return search;
    }

    @UserLoginToken
    @GetMapping("/goods/info")
    public List<Goods> goodsinfo(){
        return goodsRepository.findAll();
    }

    @UserLoginToken
    @GetMapping("/goods/info/{name}")
    public List<Goods> goodsinfo(@PathVariable("name") String name){
        return goodsRepository.findByName(name);
    }

    @UserLoginToken
    @GetMapping("/goods/info/{id}")
    public Goods goodsinfo(@PathVariable("id") Integer idInput){
        return goodsRepository.findByGoodsid(idInput);
    }

    @UserLoginToken
    @PostMapping("/order/cancel/{orderId}")
    public String ordercancel(@PathVariable("orderId") Integer id)
    {
        try {
            Order_ order_ = orderRepository.findByOrderid(id);
            Integer uid = order_.getUserid();
            Integer goodid = order_.getGoodsid();
            Goods goods = goodsRepository.findByGoodsid(goodid);
            goods.setIssale(0);
            User user = userRepository.findByUserid(uid);
            Integer sid = goods.getUserid();
            Message message = new Message();
            message.setReceiverid(sid);
            message.setMessage("用户" + user.getNickname() + "已取消购买商品" + goods.getName());
            message.setType(0);
            message.setIsread(0);
            messageRepository.save(message);
            orderRepository.deleteById(id);
            goodsRepository.save(goods);
        }
        catch (Exception e)
        {
            return "error";
        }
        finally {
            return "success";
        }
    }

    @UserLoginToken
    @PostMapping("/order/appeal")
    public String orderappeal(@RequestBody Appeal appeal)
    {
        try {
            Order_ order_ = orderRepository.findByOrderid(appeal.getOrderid());
            Integer uid = order_.getUserid();
            appeal.setUserid(uid);
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String appealTime = simpleDateFormat.format(date);
            appeal.setAppealtime(appealTime);
            appeal.setState(0);
            appealRepository.save(appeal);
        }
        catch (Exception e)
        {
            return "error";
        }
        finally {
            return "success";
        }
    }

    @UserLoginToken
    @GetMapping("/comment/list/{goodsId}/{page}/{pagesize}")
    public Page<CommentUser> getcommentinfo(@PathVariable("goodsId") Integer goodsid, @PathVariable("page") Integer page, @PathVariable("pagesize") Integer pagesize)
    {
        Pageable pageable = PageRequest.of(page-1,pagesize);
        Page<CommentUser> commentUsers = commentRepository.findCommentUserByGoodsid(goodsid,pageable);
//        Page<CommentUser> commentUsers1 = new PageImpl<>(commentUsers.subList((int) pageable.getOffset(),(int)pageable.getOffset()+pageable.getPageSize()),pageable,commentUsers.size());
        return commentUsers;
    }

    @UserLoginToken
    @PostMapping("/comment/deliver/{accountnumber}")
    public String delivercomment(@RequestBody Comment comment,
                                 @PathVariable("accountnumber") String accountnumber)
    {
        try {
            String text = comment.getText();

            if (checkApi.check(text).equals("合规")) {
                Integer muid = userRepository.findUserByAccountnumber(accountnumber).getUserid();
                comment.setUserid(muid);
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dates = dateFormat.format(date);
                comment.setReleasetime(dates);
                commentRepository.save(comment);
                Integer goodsid = comment.getGoodsid();
                String goodsname = goodsRepository.findByGoodsid(goodsid).getName();
                String comenttext = "您的" + goodsname + "商品有了新评论";
                Integer uid = goodsRepository.findByGoodsid(goodsid).getUserid();
                Message message = new Message();
                message.setReceiverid(uid);
                message.setIsread(0);
                message.setType(1);
                message.setMessage(comenttext);
                messageRepository.save(message);
                return "success";
            } else {
                return "Unqualified";
            }

        }
        catch (Exception e)
        {
            return "error";
        }

    }

    @UserLoginToken
    @PostMapping("/comment/reply/{accountnumber}/{userid}")
    public String replycomment(@RequestBody Comment comment,
                               @PathVariable("accountnumber") String accountnumber,
                               @PathVariable("userid") Integer userid)
    {
        try {
            String text = comment.getText();

            if (checkApi.check(text).equals("合规")) {
                Integer goodsid = comment.getGoodsid();
                String goodsname = goodsRepository.findByGoodsid(goodsid).getName();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dates = dateFormat.format(date);
                comment.setReleasetime(dates);
                Integer uid = userRepository.findUserByAccountnumber(accountnumber).getUserid();
                comment.setUserid(uid);
                commentRepository.save(comment);
                Message message = new Message();
                String message1 = "您的评论有了新回复";
                message.setMessage(message1);
                message.setType(1);
                message.setIsread(0);
                message.setReceiverid(userid);
                messageRepository.save(message);
                return "success";
            } else {
                return "Unqualified";
            }


        }
        catch (Exception e)
        {
            return "error";
        }

    }

    @UserLoginToken
    @PostMapping("/comment/inform/{accountnumber}")
    public String inform(@RequestBody Comment comment,@PathVariable("accountnumber") String accountnumber)
    {
        try {
            Integer commentid = comment.getCommentid();
            Integer userid = userRepository.findUserByAccountnumber(accountnumber).getUserid();
            Integer type1 = 1;
            String reason = comment.getText();
            Integer state = 0;
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dates = dateFormat.format(date);

            Inform inform = new Inform();
            inform.setObjectid(commentid);
            inform.setState(state);
            inform.setReason(reason);
            inform.setUserid(userid);
            inform.setReleasetime(dates);
            inform.setType(type1);

            informRepository.save(inform);
        }
        catch (Exception e)
        {
            return "error";
        }
        finally {
            return "success";
        }
    }

    @UserLoginToken
    @PostMapping("/goods/inform/{accountnumber}")
    public String informgoods(@RequestBody Goods goods,@PathVariable("accountnumber") String accountnumber)
    {
        try {
            Integer goodsid = goods.getGoodsid();
            Integer userid = userRepository.findUserByAccountnumber(accountnumber).getUserid();
            Integer type1 = 0;
            String reason = goods.getText();
            Integer state = 0;
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dates = dateFormat.format(date);

            Inform inform = new Inform();
            inform.setObjectid(goodsid);
            inform.setState(state);
            inform.setReason(reason);
            inform.setUserid(userid);
            inform.setReleasetime(dates);
            inform.setType(type1);

            informRepository.save(inform);
        }
        catch (Exception e)
        {
            return "error";
        }
        finally {
            return "success";
        }
    }

    @UserLoginToken
    @GetMapping("/comment/info/{id}")
    public Comment getcommentinfo(@PathVariable("id") Integer id)
    {
            Comment comment = commentRepository.findById(id).get();
            return  comment;
    }


    @PassToken
    @PostMapping("/register/{code}/{hash}/{time}")
    public String register(@PathVariable("code") String code,@PathVariable("hash") String requestHash,@PathVariable("time") String tamp,
                            @RequestBody User user)
    {
        String hash =  DigestUtils.md5DigestAsHex((KEY + "@" + tamp + "@" + code).getBytes());//生成MD5值
        if (hash.equalsIgnoreCase(requestHash)){
            System.out.println(123);
            try{
                user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
                user.setIsuse(1);
                user.setCreditrating(3.0);
                userRepository.save(user);
            }
            catch (Exception e)
            {
                return  "failed";
            }
            finally {
                return "success";
            }
        }else {
            //验证码不正确，校验失败
            return "illegal";
        }
    }

}
