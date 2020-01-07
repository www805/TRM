package com.avst.trm.v1.web.standaloneweb.service;

import com.avst.trm.v1.common.cache.ServerIpCache;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ServerconfigAndFilesave;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.poiwork.WordToHtmlUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.web.standaloneweb.req.GetAboutParam;
import com.avst.trm.v1.web.standaloneweb.vo.GetAboutVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service("aboutService")
public class AboutService extends BaseService {
    @Autowired
    private Base_serverconfigMapper base_serverconfigMapper;

    private Gson gson=new Gson();

    public void getAbout(RResult result, GetAboutParam param){
        GetAboutVO vo=new GetAboutVO();

        EntityWrapper ew=new EntityWrapper();
        ew.eq("type","police");

        List<ServerconfigAndFilesave> list=base_serverconfigMapper.getServerconfig(ew);
        if (null!=list&&list.size()>0){

            if (list.size()==1){
                String myIP = ServerIpCache.getServerIp();
                ServerconfigAndFilesave serverconfig=gson.fromJson(gson.toJson(list.get(0)), ServerconfigAndFilesave.class);
                if (null!=serverconfig){
                    vo.setCompanymsg(serverconfig.getCompanymsg());
                    vo.setSysmsg(serverconfig.getSysmsg());

                    String realurl=serverconfig.getRunbook_realurl();
                    String downurl=serverconfig.getRunbook_downurl();


                    String runbookdownurl_html=null;
                    if (StringUtils.isNotBlank(realurl)&&StringUtils.isNotBlank(downurl)){
                        //重新生成操作说明书html
                        if(realurl.endsWith(".doc")){
                            String replace = realurl.replace(".doc", ".html");
                            File f = new File(replace);
                            if (f.exists()) {
                                LogUtil.intoLog(this.getClass(),"word模板doc转html文件存在:"+replace);
                                runbookdownurl_html=downurl.replace(".doc", ".html");
                            }else {
                                WordToHtmlUtil.wordToHtml(realurl, replace);
                                LogUtil.intoLog(this.getClass(),"word模板doc转html文件不存在***************重新生成:"+replace);
                                runbookdownurl_html=downurl.replace(".doc", ".html");
                            }
                        }else if(realurl.endsWith(".docx")){
                            String replace = realurl.replace(".docx", ".html");
                            File f = new File(replace);
                            if (f.exists()) {
                                LogUtil.intoLog(this.getClass(),"word模板docx转html文件存在:"+replace);
                                runbookdownurl_html=downurl.replace(".docx", ".html");
                            }else {
                                WordToHtmlUtil.wordToHtml(realurl, replace);
                                LogUtil.intoLog(this.getClass(),"word模板doc转html文件不存在***************重新生成:"+replace);
                                runbookdownurl_html=downurl.replace(".docx", ".html");
                            }
                        }
                    }
                    String uploadpath=PropertiesListenerConfig.getProperty("upload.basepath");
                    vo.setRunbookdownurl_html(uploadpath+runbookdownurl_html);
                    vo.setRunbookdownurl(uploadpath+downurl);
                }
                result.setData(vo);
                changeResultToSuccess(result);
                return;
            }else{
                LogUtil.intoLog(this.getClass(),"多个系统配置异常--");
                result.setMessage("系统异常");
                return;
            }
        }else {
            result.setMessage("系统异常");
        }
    }
}
