package com.cccc.config;

import com.cccc.interceptor.AdminInterceptor;
import com.cccc.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebCongig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        登录拦截
        registry.addInterceptor(new LoginInterceptor())
//                放行的路径
                .excludePathPatterns("/user/login","/error")
//                拦截的所有路径
                .addPathPatterns("/**")
                .order(1);
        registry.addInterceptor(new AdminInterceptor())
//                用于权限拦截，权限为（0），只对拥有管理员权限的放行，判断不在这边
                .addPathPatterns("/user/admin/**","/course/admin/**","/all_lass/admin/**").order(2);
    }
}
