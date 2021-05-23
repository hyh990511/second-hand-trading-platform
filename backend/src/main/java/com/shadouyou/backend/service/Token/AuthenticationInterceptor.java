package com.shadouyou.backend.service.Token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.shadouyou.backend.entity.Administrator;
import com.shadouyou.backend.entity.User;
import com.shadouyou.backend.repository.AdministratorRepository;
import com.shadouyou.backend.repository.UserRepository;
import com.shadouyou.backend.service.Exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdministratorRepository administratorRepository;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new BizException("401","无token，请重新登录");
                }
                // 获取 token 中的 user id
                String userId;
                try {
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new BizException("404","未找到该资源");
                }
                Integer uid = Integer.valueOf(userId);
                User user = userRepository.findByUserid(uid);
                if (user == null) {
                    throw new BizException("404","未找到该资源");
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new BizException("401","请求的数字签名不匹配!");
                }
                return true;
            }
        }
        //检查有没有需要管理员权限的注解
        if (method.isAnnotationPresent(AdmiLoginToken.class)) {
            AdmiLoginToken admiLoginToken = method.getAnnotation(AdmiLoginToken.class);
            if (admiLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new BizException("401","无token，请重新登录");
                }
                // 获取 token 中的 admiId
                String admiId;
                try {
                    admiId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new BizException("404","未找到该资源");
                }
                Integer aid = Integer.valueOf(admiId);
                Administrator administrator = administratorRepository.findByAdministratorid(aid);
                if (administrator == null) {
                    throw new BizException("404","未找到该资源");
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(administrator.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new BizException("401","请求的数字签名不匹配!");
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }
}