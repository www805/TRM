package com.avst.trm.v1.web.cweb.service.courtService;

import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfograde;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordToProblem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Userinfograde;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_userinfogradeMapper;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.poiwork.HtmlToWord;
import com.avst.trm.v1.common.util.poiwork.WordToHtmlUtil;
import com.avst.trm.v1.web.cweb.cache.RecordrealingCache;
import com.avst.trm.v1.web.cweb.req.courtreq.*;
import com.avst.trm.v1.web.cweb.req.policereq.UploadWordTemplateParam;
import com.avst.trm.v1.web.cweb.service.policeservice.RecordService2;
import com.avst.trm.v1.web.cweb.vo.courtvo.GetUserinfogradeByssidVO;
import com.avst.trm.v1.web.cweb.vo.courtvo.GetUserinfogradePageVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("courtService")
public class CourtService extends BaseService {
    @Autowired
    private Police_userinfogradeMapper police_userinfogradeMapper;

    private Gson gson=new Gson();

    @Autowired
    private RecordService2 recordService2;


    public void getUserinfogradePage(RResult result, GetUserinfogradePageParam param){
        GetUserinfogradePageVO vo=new GetUserinfogradePageVO();
        if (null==param){
            result.setMessage("参数为空");
            LogUtil.intoLog(2,this.getClass(),"CourtService__getUserinfogradePage__param is null__"+param);
            return;
         }

        EntityWrapper getew=new EntityWrapper();
         String gradename=param.getGradename();
         if (StringUtils.isNotBlank(gradename)){
             getew.eq("gradename",gradename);
         }

        getew.orderBy("gradetype",true);
        getew.orderBy("createtime",false);
        int count=police_userinfogradeMapper.countgetUserinfogradePage(getew);
        param.setRecordCount(count);

        Page<Userinfograde> page=new Page<Userinfograde>(param.getCurrPage(),param.getPageSize());
        List<Userinfograde> list=police_userinfogradeMapper.getUserinfogradePage(page,getew);
        vo.setPageparam(param);

        if (null!=list&&list.size()>0){
            vo.setPagelist(list);
        }

        vo.setPagelist(list);
        result.setData(vo);
        changeResultToSuccess(result);
        return;

    }

    public void getUserinfogradeByssid(RResult result, GetUserinfogradeByssidParam param){
        GetUserinfogradeByssidVO vo=new GetUserinfogradeByssidVO();
        if (null==param){
            result.setMessage("参数为空");
            LogUtil.intoLog(2,this.getClass(),"CourtService__getUserinfogradeByssid__param is null__"+param);
            return;
        }
        String ssid=param.getSsid();
        if (StringUtils.isNotBlank(ssid)){
            Police_userinfograde police_userinfograde=new Police_userinfograde();
            police_userinfograde.setSsid(ssid);
            police_userinfograde = police_userinfogradeMapper.selectOne(police_userinfograde);
            if (null!=police_userinfograde){
                Userinfograde userinfograde=gson.fromJson(gson.toJson(police_userinfograde),Userinfograde.class);
                if (null!=userinfograde){
                    vo.setUserinfograde(userinfograde);
                    result.setData(vo);
                    changeResultToSuccess(result);
                    return;
                }
            }
        }
        return;
    }

    public void addUserinfograde(RResult result, AddUserinfogradeParam param){
        if (null==param){
            result.setMessage("参数为空");
            LogUtil.intoLog(2,this.getClass(),"CourtService__addUserinfograde__param is null__"+param);
            return;
        }
        Police_userinfograde police_userinfograde=gson.fromJson(gson.toJson(param),Police_userinfograde.class);
        if (null!=police_userinfograde){


            EntityWrapper ordernum_param=new EntityWrapper();
            ordernum_param.eq("gradename",police_userinfograde.getGradename());
            List<Police_userinfograde> police_userinfogrades=police_userinfogradeMapper.selectList(ordernum_param);
            if (null!=police_userinfogrades&&police_userinfogrades.size()>0){
                result.setMessage("级别名称已存在");
                return;
            }

            police_userinfograde.setCreatetime(new Date());
            police_userinfograde.setSsid(OpenUtil.getUUID_32());
            police_userinfograde.setTypessid("13");//法院类型13
            int police_userinfogradeMapper_insert_bool = police_userinfogradeMapper.insert(police_userinfograde);
            if (police_userinfogradeMapper_insert_bool >0) {
                result.setData(police_userinfogradeMapper_insert_bool);
                changeResultToSuccess(result);
                LogUtil.intoLog(1,this.getClass(),"police_userinfogradeMapper_insert_bool_"+police_userinfogradeMapper_insert_bool);
                return;
            }
        }
        return;
    }

    public void updateUserinfograde(RResult result, UpdateUserinfogradeParam param){
        if (null==param){
            result.setMessage("参数为空");
            LogUtil.intoLog(2,this.getClass(),"CourtService__updateUserinfograde__param is null__"+param);
            return;
        }

        Police_userinfograde police_userinfograde=gson.fromJson(gson.toJson(param),Police_userinfograde.class);
        if (null!=police_userinfograde){
            String ssid=police_userinfograde.getSsid();
            if (null!=ssid){

                EntityWrapper ordernum_param=new EntityWrapper();
                ordernum_param.eq("gradename",police_userinfograde.getGradename());
                ordernum_param.ne("ssid",ssid);
                List<Police_userinfograde> police_userinfogrades=police_userinfogradeMapper.selectList(ordernum_param);
                if (null!=police_userinfogrades&&police_userinfogrades.size()>0){
                    result.setMessage("级别名称已存在");
                    return;
                }


                EntityWrapper ew=new EntityWrapper();
                ew.eq("ssid",ssid);
                int police_userinfogradeMapper_update_bool = police_userinfogradeMapper.update(police_userinfograde,ew);
                if (police_userinfogradeMapper_update_bool >0) {
                    result.setData(police_userinfogradeMapper_update_bool);
                    changeResultToSuccess(result);
                    LogUtil.intoLog(1,this.getClass(),"police_userinfogradeMapper_update_bool_"+police_userinfogradeMapper_update_bool);
                    return;
                }
            }else {
                result.setMessage("参数为空");
                LogUtil.intoLog(1,this.getClass(),"police_userinfogradeMapper_update_bool_ssid is null");
                return;
            }
        }
        return;
    }

    public  void importtemplate_ue(RResult result, ReqParam<Importtemplate_ueParam> param, MultipartFile file){
        //请求参数转换
        Importtemplate_ueParam importtemplate_ueParam=gson.fromJson(gson.toJson(param.getParam()), Importtemplate_ueParam.class);
        if (null==importtemplate_ueParam){
            result.setMessage("参数为空");
            return;
        }

        String recordssid=importtemplate_ueParam.getRecordssid();
        LogUtil.intoLog(1,this.getClass(),"导入word笔录模板____参数_____recordssid："+recordssid);
        if (StringUtils.isEmpty(recordssid)){
            result.setMessage("参数为空");
            return;
        }

        if (file.isEmpty()) {
            result.setMessage("请选择doc或者docx文件进行导入");
            return;
        }
        String filename=file.getOriginalFilename();
        if (filename.endsWith(".doc")||filename.endsWith(".docx")){
            InputStream inputStream = null;
            try {
                inputStream = file.getInputStream();
                String html= WordToHtmlUtil.wordToHtml_in2str(inputStream,filename);
                //替换对应笔录参数===start
                Map<String,String> dataMap=recordService2.exportData(recordssid,false);
                if (dataMap != null) {
                    for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                        html = html.replace(entry.getKey(), entry.getValue());
                    }
                }
                //替换对应笔录参数===end
                result.setData(html);
                changeResultToSuccess(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(null != inputStream){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            result.setMessage("请选择doc或者docx文件进行导入");
            return;
        }
        return;
    }

    public void exporttemplate_ue(RResult result, Exporttemplate_ueParam param){
        String recordssid=param.getRecordssid();
        if (StringUtils.isEmpty(recordssid)){
            result.setMessage("参数为空");
            return;
        }

        List<RecordToProblem> recordToProblems= RecordrealingCache.getRecordrealByRecordssid(recordssid);//笔录携带的题目答案集合
        if (null!=recordToProblems&&recordToProblems.size()>0){
            String content="";
           for (RecordToProblem recordToProblem : recordToProblems) {
                content+=recordToProblem.getProblem();
            }
            HtmlToWord.HtmlToWord("f:\\1.doc",content);
        }
        return;
    }




}
