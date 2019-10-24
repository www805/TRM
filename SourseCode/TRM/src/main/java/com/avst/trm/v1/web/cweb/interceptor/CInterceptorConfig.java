package com.avst.trm.v1.web.cweb.interceptor;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration          //使用注解 实现拦截
public class CInterceptorConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录拦截的管理器
        InterceptorRegistration registration = registry.addInterceptor(new CManagerInterceptor());     //拦截的对象会进入这个类中进行判断
        registration.addPathPatterns("/cweb/base/**");
        registration.addPathPatterns("/cweb/");
        registration.excludePathPatterns("/login", "/error", "/static/**", "/static/layui/iconz", "/logout", "/cweb/base/main/getNavList","/cweb/base/main/getPant","/cweb/base/main/updatePassWord","/cweb/base/main/gotoforgotpassword","/cweb/base/main/uploadkey");       //添加不拦截路径

        //权限拦截的管理器
    }


}
