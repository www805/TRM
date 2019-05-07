package com.avst.trm.v1.web.cweb.interceptor;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.util.baseaction.CodeForSQ;
import com.avst.trm.v1.web.cweb.param.InitVO;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 管理员拦截器
 */
public class CManagerInterceptor extends HandlerInterceptorAdapter {

    //在控制器执行前调用
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        System.out.println("执行preHandle方法-->01");


        //获取session，判断用户
        HttpSession session=request.getSession();

        boolean disbool=true;
        InitVO initVO;
        if(null==session.getAttribute(Constant.INIT_CLIENT)){//web客户端页面动作集
            disbool=false;
            initVO=CommonCache.getinit_CLIENT();
            session.setAttribute(Constant.INIT_CLIENT,initVO);
        }else{
            initVO=(InitVO)session.getAttribute(Constant.INIT_CLIENT);
        }
        session.setAttribute(Constant.INIT_CLIENTKEY,CommonCache.getClientKey());


        String url=request.getRequestURI();
        if( url.endsWith("/cweb/base/main/gotologin")|| url.endsWith("/cweb/base/main/userlogin")){//跳过进入登录页面的拦截
            return true;
        }


        if(null==initVO||!initVO.getCode().equals(CodeForSQ.TRUE)){//看web客户端页面动作集是否有效
            disbool=false;
        }
        String basepath="/cweb/base/main"; //首页的action只允许在homeaction里面
        String forstpageid=basepath+"/gotologin";
        //判断session中的用户信息是否可以通过
        if(null==session.getAttribute(Constant.MANAGE_CLIENT)){
            disbool=false;
        }else{
            String pageid=initVO.getFirstpageid();
            forstpageid=basepath+ ( pageid.startsWith("/") ? pageid : ("/"+pageid) );
        }

//        disbool = true;  //暂时让他成功
        if (disbool) {
            return true;  //通过拦截器，继续执行请求
        } else {//跳转登录界面
            request.getRequestDispatcher(forstpageid).forward(request, response);
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
