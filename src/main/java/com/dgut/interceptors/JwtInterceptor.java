package com.dgut.interceptors;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dgut.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String,Object> map = new HashMap<>();
        //获取请求头中的令牌
        String token = request.getHeader("token");
        try {
            JwtUtil.verify(token);//验证令牌
            return true;
        }catch (SignatureVerificationException e){
            e.printStackTrace();
            map.put("msg", "无效签名！");
        }catch (TokenExpiredException e){
            e.printStackTrace();
            map.put("msg", "token过期！");
        }catch (AlgorithmMismatchException e){
            e.printStackTrace();
            map.put("msg", "token算法不一致！");
        }catch (Exception e){
            e.printStackTrace();
            map.put("msg", "token无效！");
        }
        map.put("state", false);
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
