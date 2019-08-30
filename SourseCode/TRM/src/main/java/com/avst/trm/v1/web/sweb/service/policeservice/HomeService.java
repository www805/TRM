package com.avst.trm.v1.web.sweb.service.policeservice;

import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.AppServiceCache;
import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.*;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.common.util.sq.SQEntity;
import com.avst.trm.v1.common.util.sq.SQGN;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HomeService extends BaseService {

    @Autowired
    private Police_templateMapper policeTemplateMapper; //模板

    @Autowired
    private Police_problemMapper policeProblemMapper; //问题

    @Autowired
    private Police_answerMapper policeAnswerMapper; //答案

    @Autowired
    private Police_arraignmentMapper policeArraignmentMapper;   //提讯

    @Autowired
    private Police_caseMapper policeCaseMapper;    //案件

    @Autowired
    private Police_recordtypeMapper policeRecordtypeMapper; //笔录类型

    @Autowired
    private Base_keywordMapper keywordMapper;   //关键字

    @Autowired
    private Base_admininfoMapper admininfoMapper;   //系统用户

    @Autowired
    private Base_roleMapper roleMapper; //角色

    @Autowired
    private Base_filesaveMapper filesaveMapper;     //文件存储

    @Autowired
    private Base_serverconfigMapper base_serverconfigMapper;    //服务器系统配置

    @Autowired
    private Base_serverconfigMapper serverconfigMapper;


    public void getAllCount(RResult rResult, Model model) {

        Integer policeTemplateCount = policeTemplateMapper.selectCount(null);
        Integer policeProblemCount = policeProblemMapper.selectCount(null);
        Integer policeAnswerCount = policeAnswerMapper.selectCount(null);
        Integer policeArraignmentCount = policeArraignmentMapper.selectCount(null);
        Integer policeCaseCount = policeCaseMapper.selectCount(null);
        Integer policeRecordtypeCount = policeRecordtypeMapper.selectCount(null);
        Integer keywordCount = keywordMapper.selectCount(null);
        Integer admininfoCount = admininfoMapper.selectCount(null);
        Integer roleCount = roleMapper.selectCount(null);
        Integer fileCount = filesaveMapper.selectCount(null);

        CommonCache.gnlist();
        SQEntity getSQEntity = CommonCache.getSQEntity;//获取系统授权信息

        //授权功能
        List gnArrayList = new ArrayList();
        String gnlist = getSQEntity.getGnlist();
        String[] strings = gnlist.split("\\|");
        if (null != strings && strings.length > 0) {

            for (int i = 0; i < strings.length; i++) {
                String str = strings[i];
                if("asr".equals(str)){
                    gnArrayList.add(SQGN.asr);
                }else if("fd".equals(str)){
                    gnArrayList.add(SQGN.fd);
                }else if("ph".equals(str)){
                    gnArrayList.add(SQGN.ph);
                }else if("record".equals(str)){
                    gnArrayList.add(SQGN.record);
                }else if("tts".equals(str)){
                    gnArrayList.add(SQGN.tts);
                }
            }
        }

        Integer dayNum = 0;
//        Base_serverconfig serverconfig = base_serverconfigMapper.selectById(1);
//        if (null != serverconfig) {
//            dayNum = serverconfig.getWorkdays();
//        }

        model.addAttribute("policeTemplateCount", policeTemplateCount);
        model.addAttribute("policeProblemCount", policeProblemCount);
        model.addAttribute("policeAnswerCount", policeAnswerCount);
        model.addAttribute("policeArraignmentCount", policeArraignmentCount);
        model.addAttribute("policeCaseCount", policeCaseCount);
        model.addAttribute("policeRecordtypeCount", policeRecordtypeCount);
        model.addAttribute("keywordCount", keywordCount);
        model.addAttribute("fileCount", fileCount);
        model.addAttribute("admininfoCount", admininfoCount);
        model.addAttribute("roleCount", roleCount);
        model.addAttribute("getSQEntity", getSQEntity);
        model.addAttribute("getGnlist", gnArrayList);
        model.addAttribute("workdays", dayNum);

    }

    public void getNavList(RResult result) {

        AppCacheParam cacheParam = AppServiceCache.getAppServiceCache();
        String nav_file_name=PropertiesListenerConfig.getProperty("nav.file.name");
        String path = OpenUtil.getXMSoursePath() + "\\" + nav_file_name + ".yml";
        if(null == cacheParam.getData()){
            FileInputStream fis = null;
            try {

                Base_serverconfig serverconfig = serverconfigMapper.selectById(1);

                if (StringUtils.isNotEmpty(serverconfig.getSyslogo_filesavessid())) {
                    Base_filesave filesaveSyslogo = new Base_filesave();
                    filesaveSyslogo.setSsid(serverconfig.getSyslogo_filesavessid());
                    Base_filesave syslogo = filesaveMapper.selectOne(filesaveSyslogo);
                    if (null!=syslogo){
                        cacheParam.setSyslogoimage(syslogo.getRecorddownurl());
                    }
                }

                if (StringUtils.isNotEmpty(serverconfig.getClient_filesavessid())) {
                    Base_filesave filesaveClientlogo = new Base_filesave();
                    filesaveClientlogo.setSsid(serverconfig.getClient_filesavessid());
                    Base_filesave clientlogo = filesaveMapper.selectOne(filesaveClientlogo);
                    if (null!=clientlogo){
                        cacheParam.setClientimage(clientlogo.getRecorddownurl());
                    }
                }

                fis = new FileInputStream(path);

                Yaml yaml = new Yaml();
                Map<String,Object> map = yaml.load(fis);

                String application_name= PropertiesListenerConfig.getProperty("spring.application.name");
                String swebFile=PropertiesListenerConfig.getProperty("nav.file.service");

                Map<String,Object> avstYml = (Map<String, Object>) map.get(application_name);
                Map<String,Object> fileYml = (Map<String, Object>) avstYml.get(swebFile);
                Map<String,Object> zkYml = (Map<String, Object>) map.get("zk");
                Map<String,Object> guidepage = (Map<String, Object>) zkYml.get("guidepage");
                String guidepageUrl = (String) guidepage.get("url");
                fileYml.put("bottom", map.get("bottom"));
                String hostAddress = NetTool.getMyIP();

                cacheParam.setData(fileYml);
                cacheParam.setGuidepageUrl("http://" + hostAddress + guidepageUrl);

            } catch (IOException e) {
                LogUtil.intoLog(4, this.getClass(), "没找到外部配置文件：" + path);
            }finally {
                if(null != fis){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        result.setData(cacheParam);
        changeResultToSuccess(result);
    }
}
