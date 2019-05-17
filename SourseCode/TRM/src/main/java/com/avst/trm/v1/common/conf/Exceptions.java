package com.avst.trm.v1.common.conf;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.RResult;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常捕捉类
 *
 * @author Bean
 *
 */
@RestControllerAdvice
public class Exceptions {

    @ExceptionHandler({UnauthorizedException.class})
    @ResponseBody
    public RResult UnauthorizedException(NativeWebRequest request, UnauthorizedException e) {
        RResult result = new RResult();
        result.setMessage("权限不足");
        result.setEndtime(DateUtil.getDateAndMinute());
        result.setData(null);
        System.out.println("异常信息:"+e.getMessage());
        return result;
    }

    @ExceptionHandler(value = UnauthenticatedException.class)
    @ResponseBody
    public RResult UnauthenticatedException(UnauthorizedException e) {
        RResult result = new RResult();
        result.setMessage("未认证");
        result.setEndtime(DateUtil.getDateAndMinute());
        result.setData(null);
        System.out.println("异常信息:"+e.getMessage());
        return result;
    }

   /* // 其它异常异常捕捉
    @ResponseBody
    @ExceptionHandler(value =Exception.class)
    public RResult errorHandler(java.lang.Exception e) {
        RResult result = new RResult();
        result.setMessage(e.getMessage());
        result.setEndtime(DateUtil.getDateAndMinute());
        result.setData(null);
        System.out.println("异常信息:"+e.getMessage());
        return result;
    }*/


}