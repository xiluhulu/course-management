package com.cccc.interceptor;

import com.cccc.utils.Jwt_Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Component
//登录验证
public class TeaInterceptor implements HandlerInterceptor {
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
        System.out.println("33333333333333333333333");
//        获取请求头的token
        String token = request.getHeader("token");
        System.out.println(token);
//        解析token
        Claims claims = Jwt_Utils.parseToken(token);
//        获取用户权限
        String role = (String) claims.get("role");
        System.out.println(role);
//        判断用户权限
        if ("1".equals(role)){return true;}
        HashMap<String, Object> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("code",406);
        stringStringHashMap.put("msg","token错误");
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(stringStringHashMap);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(s);
        return false;

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
