package com.avst.trm.v1.outsideinterface.serverinterface;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RRParam;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.outsideinterface.conf.OutSideIntegerfaceCache;
import com.avst.trm.v1.outsideinterface.reqparam.BaseReqParam;
import com.avst.trm.v1.outsideinterface.serverinterface.police.v1.service.ForDownServerService_police;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 对下级服务器提供的同步接口
 */
@RestController
@RequestMapping("/forDownServer")
public class ForDownServerBaseAction extends BaseAction {


    private ForDownServerBaseServiceInterface forDownServerBaseServiceInterface;

    @RequestMapping("/initsynchronizeddata")
    @ResponseBody
    public RResult initsynchronizeddata(String sqCode,String sqNum){
        RResult rResult=createNewResultOfFail();
        if(StringUtils.isEmpty(sqCode)||StringUtils.isEmpty(sqNum)){
            rResult.setMessage("授权为空，请检测授权");
            rResult.setEndtime(DateUtil.getDateAndMinute());
            return rResult;
        }
        getForDownServerBaseServiceImpl().initsynchronizeddata(sqCode,sqNum,rResult);

        return rResult;
    };
    @RequestMapping("/startSynchronizedata")
    @ResponseBody
    public  RResult startSynchronizedata(BaseReqParam param){
        RResult rResult=createNewResultOfFail();
        RRParam rr=checkToken(param.getSqNum(),param.getToken());
        if(null==rr||rr.getCode()!=0){
            rResult.setMessage(rr.getMessage());
            rResult.setEndtime(DateUtil.getDateAndMinute());
            return rResult;
        }
        getForDownServerBaseServiceImpl().startSynchronizedata(param,rResult);
        return rResult;
    };

    /**
     * 上传数据
     * @param param
     * @return
     */
    @RequestMapping("/synchronizedata")
    @ResponseBody
    public  RResult synchronizedata(BaseReqParam param){
        RResult rResult=createNewResultOfFail();
        RRParam rr=checkToken(param.getSqNum(),param.getToken());
        if(null==rr||rr.getCode()!=0){
            rResult.setMessage(rr.getMessage());
            rResult.setEndtime(DateUtil.getDateAndMinute());
            return rResult;
        }
        getForDownServerBaseServiceImpl().synchronizedata(param,rResult,null,1);
        return rResult;
    };

    /**
     * 上传文件
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("/synchronizedata_file")
    @ResponseBody
    public  RResult synchronizedata_file(@RequestParam("file") MultipartFile file, HttpServletRequest request,BaseReqParam param){
        RResult rResult=createNewResultOfFail();

        try{

            RRParam rr=checkToken(param.getSqNum(),param.getToken());
            if(null==rr||rr.getCode()!=0){
                rResult.setMessage(rr.getMessage());
                rResult.setEndtime(DateUtil.getDateAndMinute());
                return rResult;
            }

            if(null==file){//基本上不可能出现
                rResult.setMessage("文件未找到");
                return rResult;
            }

            getForDownServerBaseServiceImpl().synchronizedata(param,rResult,file,2);

        }catch (Exception e){
            e.printStackTrace();
        }
        return rResult;
    };


    @RequestMapping("/overSynchronizedata")
    @ResponseBody
    public  RResult overSynchronizedata(BaseReqParam param){
        RResult rResult=createNewResultOfFail();
        RRParam rr=checkToken(param.getSqNum(),param.getToken());
        if(null==rr||rr.getCode()!=0){
            rResult.setMessage(rr.getMessage());
            rResult.setEndtime(DateUtil.getDateAndMinute());
            return rResult;
        }
        getForDownServerBaseServiceImpl().overSynchronizedata(param,rResult);
        return rResult;
    };


    /**
     * 根据类型查找对应的处理类
     * @return
     */
    private ForDownServerBaseServiceInterface getForDownServerBaseServiceImpl(){

        if(null!=forDownServerBaseServiceInterface){
            return forDownServerBaseServiceInterface;
        }

        String serverType= null;
        try {
            serverType = CommonCache.getCurrentServerType();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(serverType.startsWith("court")){

        }else if(serverType.startsWith("police")){
            forDownServerBaseServiceInterface= (ForDownServerService_police)SpringUtil.getBean("ForDownServerService_police");
        }else if(serverType.startsWith("meeting")){

        }else if(serverType.startsWith("dis")){

        }
        return forDownServerBaseServiceInterface;
    }

    /**
     * 暂时当前同步一个下级服务器只能有一个
     * @param sqNum
     * @param token
     * @return
     */
    private RRParam checkToken(String sqNum, String token){

        RRParam rrParam=new RRParam();
        String token_server=OutSideIntegerfaceCache.getToupserverTBToken(sqNum);

        if(StringUtils.isEmpty(token)){
            rrParam.setCode(-2);
            rrParam.setMessage("传到上级服务器的token为空");
        }else if(StringUtils.isEmpty(token_server)){
            rrParam.setCode(-1);
            rrParam.setMessage("上级服务器没有这个token，请初始化同步");
        }else{
            if(token_server.equals(token)){
                rrParam.setCode(0);
                rrParam.setMessage("正常同步中");
            }else{
                rrParam.setCode(-3);
                rrParam.setMessage("token不一致，请确认");
            }
        }
        return rrParam;
    }


}
