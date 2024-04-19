package com.cccc.interceptor;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.druid.support.json.JSONWriter;
import com.cccc.common.R;
import com.cccc.utils.Jwt_Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

//登录验证
@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 控制器执行之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("111111111111111111111111111");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        ObjectMapper objectMapper = new ObjectMapper();
//        从请求头获取token
        String token = request.getHeader("token");
        if (token==null || token.isEmpty()){
            HashMap<String, Object> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("code",405);
            stringStringHashMap.put("msg","未登录！");
            String s = objectMapper.writeValueAsString(stringStringHashMap);
            response.getWriter().println(s);
            return false;        }
//        解析token
        try {
            Claims claims = Jwt_Utils.parseToken(token);
            Object id = claims.get("id");
            return true;
        } catch (ExpiredJwtException e) {
//            token过期
            HashMap<String, Object> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("code",405);
            stringStringHashMap.put("msg","token过期");
            String s = objectMapper.writeValueAsString(stringStringHashMap);
            response.getWriter().println(s);
            return false;
        } catch (Exception e) {
//            token不合法
            HashMap<String, Object> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("code",405);
            stringStringHashMap.put("msg","token不法");
            String s = objectMapper.writeValueAsString(stringStringHashMap);
            response.getWriter().println(s);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 控制器执行完后
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }


}
