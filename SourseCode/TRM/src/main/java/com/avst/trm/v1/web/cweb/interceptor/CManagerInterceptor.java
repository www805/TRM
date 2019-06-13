package com.avst.trm.v1.web.cweb.interceptor;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.conf.socketio.NettySocketConfig;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.baseaction.CodeForSQ;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 客户端管理员拦截器
 */
public class CManagerInterceptor extends HandlerInterceptorAdapter {

    //在控制器执行前调用
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        LogUtil.intoLog(this.getClass(),"执行preHandle方法-->01");


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
        String clientkey=CommonCache.getClientKey();
        if(null!=clientkey){
            session.setAttribute(Constant.INIT_CLIENTKEY,clientkey);
        }

        String socketio_port=PropertiesListenerConfig.getProperty("socketio.server.port");
        String socketio_host=PropertiesListenerConfig.getProperty("socketio.server.host");
        if (null!= socketio_host&&null!= socketio_port){
            session.setAttribute(Constant.SOCKETIO_HOST,socketio_host);
            session.setAttribute(Constant.SOCKETIO_PORT,socketio_port);
        }

        String url=request.getRequestURI();
        if( url.endsWith("/cweb/base/main/gotologin")|| url.endsWith("/cweb/base/main/userlogin")){//跳过进入登录页面的拦截
            return true;
        }

        if(null==initVO||!initVO.getCode().equals(CodeForSQ.TRUE)){//看web客户端页面动作集是否有效
            disbool=false;
        }
        String basepath="/cweb/base/main"; //首页的action只允许在homeaction里面
        String firstinterface=basepath+"/gotologin";
        //判断session中的用户信息是否可以通过
        if(null==session.getAttribute(Constant.MANAGE_CLIENT)){
            disbool=false;
        }else{
            String interfaceurl=initVO.getFirstinterface();
            firstinterface= ( interfaceurl.startsWith("/") ? interfaceurl : ("/"+interfaceurl) );
        }

//        disbool = true;  //暂时让他成功
        if (disbool&&!url.equals("/cweb/")) {
            return true;  //通过拦截器，继续执行请求
        } else {//跳转登录界面
            LogUtil.intoLog(this.getClass(),firstinterface+":firstinterface");
            request.getRequestDispatcher(firstinterface).forward(request, response);
            return false;  //没有通过拦截器，返回登录页面
        }
    }
    //在后端控制器执行后调用
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        LogUtil.intoLog(this.getClass(),"执行postHandle方法-->02");
        super.postHandle(request, response, handler, modelAndView);
    }
    //整个请求执行完成后调用
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        LogUtil.intoLog(this.getClass(),"执行afterCompletion方法-->03");
        super.afterCompletion(request, response, handler, ex);
    }

}
