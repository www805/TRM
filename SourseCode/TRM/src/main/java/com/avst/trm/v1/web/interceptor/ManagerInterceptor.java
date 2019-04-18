package com.avst.trm.v1.web.interceptor;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.web.vo.InitVO;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 管理员拦截器
 */
public class ManagerInterceptor extends HandlerInterceptorAdapter {

    //在控制器执行前调用
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        System.out.println("执行preHandle方法-->01");

        //获取session，判断用户
        HttpSession session=request.getSession();

        boolean disbool=true;
        InitVO initVO;
        String servertype=CommonCache.getCurrentServerType();
        if(null==session.getAttribute(Constant.INIT_WEB)){
            disbool=false;
            CommonCache.getActionList();



        }else{
            initVO=(InitVO)session.getAttribute(Constant.INIT_WEB);
        }


        //判断session中的用户信息是否可以通过

        if (true) {
            return true;  //通过拦截器，继续执行请求
        } else {//跳转登录界面
            request.getRequestDispatcher("/login").forward(request, response);
            return false;  //没有通过拦截器，返回登录页面
        }
    }
    //在后端控制器执行后调用
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        System.out.println("执行postHandle方法-->02");
        super.postHandle(request, response, handler, modelAndView);
    }
    //整个请求执行完成后调用
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println("执行afterCompletion方法-->03");
        super.afterCompletion(request, response, handler, ex);
    }


}
