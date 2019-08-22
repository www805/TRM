package com.avst.trm.v1.web.cweb.interceptor;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.baseaction.CodeForSQ;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.AnalysisSQ;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 客户端管理员拦截器
 */
public class CManagerInterceptor extends HandlerInterceptorAdapter {

    //在控制器执行前调用
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        LogUtil.intoLog(this.getClass(),"执行preHandle方法-->01");


        try {
            //获取session，判断用户
            HttpSession session=request.getSession();

            boolean disbool=true;
            InitVO initVO;
            if(null==session.getAttribute(Constant.INIT_CLIENT)
                ||CommonCache.initbool){//web客户端页面动作集
                disbool=false;
                initVO=CommonCache.getinit_CLIENT();
                session.setAttribute(Constant.INIT_CLIENT,initVO);
                if(null!=session.getAttribute(Constant.INIT_CLIENT)){
                    CommonCache.initbool=false;//只能用一次初始化判断
                }
                LogUtil.intoLog(3,this.getClass(),"测试7");
            }else{
                initVO=(InitVO)session.getAttribute(Constant.INIT_CLIENT);
            }
            LogUtil.intoLog(3,this.getClass(),"测试6");
            String clientkey=CommonCache.getClientKey();
            if(null!=clientkey){
                session.setAttribute(Constant.INIT_CLIENTKEY,clientkey);
            }else{
                clientkey = AnalysisSQ.getClientKey();
                session.setAttribute(Constant.INIT_CLIENTKEY,clientkey);
                LogUtil.intoLog(this.getClass(),"clientkey is null 53,clientkey="+clientkey);
            }
            LogUtil.intoLog(3,this.getClass(),"测试5");
            if(null==session.getAttribute(Constant.SOCKETIO_HOST)||
                    null==session.getAttribute(Constant.SOCKETIO_PORT)){
                String socketio_host=PropertiesListenerConfig.getProperty("socketio.server.host");
                String socketio_port=PropertiesListenerConfig.getProperty("socketio.server.port");
                if (null!= socketio_host&&null!= socketio_port){
                    session.setAttribute(Constant.SOCKETIO_HOST,socketio_host);
                    session.setAttribute(Constant.SOCKETIO_PORT,socketio_port);
                }
            }
            LogUtil.intoLog(this.getClass(),"测试1");
            String url=request.getRequestURI();
            if( url.endsWith("/cweb/base/main/gotologin")|| url.endsWith("/cweb/base/main/userlogin")){//跳过进入登录页面的拦截
                LogUtil.intoLog(this.getClass(),url+":url，跳过进入登录页面的拦截");
                return true;
            }
            if( url.endsWith("/uploadNotification")){//测试上传接口
                LogUtil.intoLog(this.getClass(),url+":url，测试上传接口");
                return true;
            }
            LogUtil.intoLog(3,this.getClass(),"测试2");

            String basepath="/cweb/base/main"; //首页的action只允许在homeaction里面
            String firstinterface=basepath+"/gotologin";
            if(null==initVO||!initVO.getCode().equals(CodeForSQ.TRUE)){//看web客户端页面动作集是否有效
                disbool=false;
            }else{
                //判断session中的用户信息是否可以通过
                if(null==session.getAttribute(Constant.MANAGE_CLIENT)){
                    disbool=false;
                }else{
                    String interfaceurl=initVO.getFirstinterface();
                    firstinterface= ( interfaceurl.startsWith("/") ? interfaceurl : ("/"+interfaceurl) );
                }
            }
            LogUtil.intoLog(3,this.getClass(),"测试3");
//        disbool = true;  //暂时让他成功
            if (disbool&&!url.equals("/cweb/")) {
                LogUtil.intoLog(3,this.getClass(),"测试4");
                return true;  //通过拦截器，继续执行请求
            } else {//跳转登录界面
                LogUtil.intoLog(3,this.getClass(),firstinterface+":firstinterface，跳转登录界面");
                request.getRequestDispatcher(firstinterface).forward(request, response);
                return false;  //没有通过拦截器，返回登录页面
            }

        } catch (ServletException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.intoLog(this.getClass(),"clientkey is null 53,clientkey="+CommonCache.getClientKey());
        return false;  //没有通过拦截器，返回登录页面
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
