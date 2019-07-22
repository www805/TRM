package com.avst.trm.v1.web.sweb.interceptor;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration          //使用注解 实现拦截
public class InterceptorConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录拦截的管理器
        InterceptorRegistration registration = registry.addInterceptor(new ManagerInterceptor());     //拦截的对象会进入这个类中进行判断
        registration.addPathPatterns("/sweb/base/**");                    //publicweb下的路径都被拦截
        registration.addPathPatterns("/sweb/");
        registration.excludePathPatterns("/login", "/error", "/static/**", "/static/layui/iconz", "/logout", "/sweb/base/home/getNavList");       //添加不拦截路径

        //权限拦截的管理器
    }


}
