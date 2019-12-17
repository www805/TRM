package com.avst.trm.v1.web.sweb.service.policeservice;

import com.avst.trm.v1.common.cache.AppServerCache;
import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.datasourse.base.mapper.*;
import com.avst.trm.v1.common.datasourse.police.entity.Police_problem;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.sq.SQEntity;
import com.avst.trm.v1.common.util.sq.SQGN;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

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


    public void getAllCount(RResult rResult, Model model) {

        Integer policeTemplateCount = policeTemplateMapper.selectCount(null);
        Integer policeProblemCount = policeProblemMapper.selectCount(null);
//        Integer policeAnswerCount = policeAnswerMapper.selectCount(null);

        EntityWrapper<Police_problem> ew = new EntityWrapper<>();
        ew.ne("referanswer", "");
        Integer referanswerCount = policeProblemMapper.selectCount(ew);

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
                if("asr_f".equals(str)){
                    gnArrayList.add(SQGN.asr);
                }else if("fd_f".equals(str)){
                    gnArrayList.add(SQGN.fd);
                }else if("ph_f".equals(str)){
                    gnArrayList.add(SQGN.ph);
                }else if("record_f".equals(str)){
                    gnArrayList.add(SQGN.record);
                }else if("tts_f".equals(str)){
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
        model.addAttribute("referanswerCount", referanswerCount);
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
        AppCacheParam cacheParam = AppServerCache.getAppServerCache();

        result.setData(cacheParam);
        changeResultToSuccess(result);
    }
}
