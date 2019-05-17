package com.avst.trm.v1.web.sweb.service.policeservice;

import com.avst.trm.v1.common.datasourse.base.entity.Base_keyword;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_keywordMapper;
import com.avst.trm.v1.common.datasourse.police.entity.Police_answer;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.*;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.policereq.GetArraignmentListParam;
import com.avst.trm.v1.web.sweb.vo.policevo.GetArraignmentBySsidVO;
import com.avst.trm.v1.web.sweb.vo.policevo.GetArraignmentListVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("arraignmentService")
public class ArraignmentService extends BaseService {
    @Autowired
    private Police_caseMapper police_caseMapper;

    @Autowired
    private Police_casetoarraignmentMapper police_casetoarraignmentMapper;

    @Autowired
    private Police_answerMapper police_answerMapper;

    @Autowired
    private Police_recordMapper police_recordMapper;

    @Autowired
    private Base_keywordMapper base_keywordMapper;

    @Autowired
    private Police_recordtoproblemMapper police_recordtoproblemMapper;

  public void  getArraignmentList(RResult result, GetArraignmentListParam param){
        GetArraignmentListVO getArraignmentListVO=new GetArraignmentListVO();

        //请求参数组合
        EntityWrapper ew=new EntityWrapper();
        if (StringUtils.isNotBlank(param.getCasename())){
            ew.like(true,"c.casename",param.getCasename().trim());
        }
        if (StringUtils.isNotBlank(param.getCasenum())){
            ew.like(true,"c.casenum",param.getCasenum().trim());
        }
        if (StringUtils.isNotBlank(param.getUsername())){
            ew.like(true,"u.username",param.getUsername().trim());
        }

        int count = police_caseMapper.countgetArraignmentList(ew);
        param.setRecordCount(count);

        ew.orderBy("c.ordernum",true);
        ew.orderBy("c.createtime",false);
        Page<CaseAndUserInfo> page=new Page<CaseAndUserInfo>(param.getCurrPage(),param.getPageSize());
        List<CaseAndUserInfo> list=police_caseMapper.getArraignmentList(page,ew);
        getArraignmentListVO.setPageparam(param);

        if (null!=list&&list.size()>0){
            //绑定多次提讯数据
            for (CaseAndUserInfo recordAndCase : list) {
                EntityWrapper ewarraignment=new EntityWrapper();
                ewarraignment.eq("cr.casessid",recordAndCase.getSsid());
                ewarraignment.orderBy("a.createtime",false);
                List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                    recordAndCase.setArraignments(arraignmentAndRecords);
                }
            }
            getArraignmentListVO.setPagelist(list);
        }
        result.setData(getArraignmentListVO);
        changeResultToSuccess(result);
        return;
    }

    public void getArraignmentByCaseSsid(RResult result, String caseSsid){
        try {
            EntityWrapper ewarraignment=new EntityWrapper();
            ewarraignment.eq("cr.casessid",caseSsid);
            ewarraignment.orderBy("a.createtime",false);
            List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
            if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                result.setData(arraignmentAndRecords);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        changeResultToSuccess(result);
        return;
    }

    public void  getArraignmentBySsid(RResult result, String ssid){
        //查询笔录信息、案件信息、提讯信息、实时文件、全部录音文件
        GetArraignmentBySsidVO getArraignmentBySsidVO=new GetArraignmentBySsidVO();
        String recordssid=ssid;


        //全部关键字
        EntityWrapper keywordParam=new EntityWrapper();
        keywordParam.orderBy("createtime",false);
        List<Base_keyword> keywords=base_keywordMapper.selectList(keywordParam);
        if (null!=keywords&&keywords.size()>0){
            getArraignmentBySsidVO.setKeywords(keywords);
        }


        //根据笔录ssid中的笔录模板获取全部问题
        try {
            EntityWrapper ew=new EntityWrapper();
            ew.eq("r.ssid",recordssid);
            ew.orderBy("p.ordernum",true);
            ew.orderBy("p.createtime",true);
            List<RecordToProblem> problems = police_recordtoproblemMapper.getRecordToProblemByRecordSsid(ew);
            if (null!=problems&&problems.size()>0){
                //根据题目和笔录查找对应答案
                for (RecordToProblem problem : problems) {
                    String problemssid=problem.getSsid();
                    if (StringUtils.isNotBlank(problemssid)){
                        EntityWrapper answerParam=new EntityWrapper();
                        answerParam.eq("recordtoproblemssid",problemssid);
                        answerParam.orderBy("ordernum",true);
                        answerParam.orderBy("createtime",true);
                        List<Police_answer> answers=police_answerMapper.selectList(answerParam);
                        if (null!=answers&&answers.size()>0){
                            problem.setAnswers(answers);
                        }
                    }
                }
                getArraignmentBySsidVO.setProblems(problems);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //根据笔录ssid获取录音数据
        try {
            EntityWrapper recordParam=new EntityWrapper();
            recordParam.eq("r.ssid",recordssid);
            Record record=police_recordMapper.getRecordBySsid(recordParam);

            if (null!=record){
                //获取实时文件
                getArraignmentBySsidVO.setRecord(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.setData(getArraignmentBySsidVO);
        changeResultToSuccess(result);
        return;
    }

}
