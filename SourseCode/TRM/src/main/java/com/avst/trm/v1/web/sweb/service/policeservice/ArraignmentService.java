package com.avst.trm.v1.web.sweb.service.policeservice;

import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_keyword;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
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
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
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

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;

    @Autowired
    private Police_userinfoMapper police_userinfoMapper;

    private Gson gson = new Gson();

  public void  getArraignmentList(RResult result, GetArraignmentListParam param, HttpSession session){
        GetArraignmentListVO getArraignmentListVO=new GetArraignmentListVO();

        //请求参数组合
        EntityWrapper ew=new EntityWrapper();
        if (StringUtils.isNotBlank(param.getCasename())){
            ew.like(true,"c.casename",param.getCasename().trim());
        }

        if (StringUtils.isNotBlank(param.getUsername())){
            ew.like(true,"u.username",param.getUsername().trim());
        }
        if(StringUtils.isNotEmpty(param.getOccurrencetime_start()) && StringUtils.isNotEmpty(param.getOccurrencetime_end())){
              ew.between("c.occurrencetime", param.getOccurrencetime_start(), param.getOccurrencetime_end());
        }
        if(StringUtils.isNotEmpty(param.getStarttime_start()) && StringUtils.isNotEmpty(param.getStarttime_end())){
          ew.between("c.starttime", param.getStarttime_start(), param.getStarttime_end());
        }

        ew.ne("c.casebool",-1);//案件状态不能为删除状态

        int count = police_caseMapper.countgetCaseList(ew);
        param.setRecordCount(count);

        ew.orderBy("c.ordernum",true);
        ew.orderBy("c.createtime",false);
        Page<Case> page=new Page<Case>(param.getCurrPage(),param.getPageSize());
        List<Case> list=police_caseMapper.getCaseList(page,ew);
        getArraignmentListVO.setPageparam(param);

        if (null!=list&&list.size()>0){
            for (Case case_: list) {
                //1、绑定多次提讯数据
                EntityWrapper ewarraignment=new EntityWrapper();
                ewarraignment.eq("cr.casessid",case_.getSsid());
                ewarraignment.ne("r.recordbool",-1);//笔录状态不为删除状态
                ewarraignment.orderBy("a.createtime",false);
                List<ArraignmentAndRecord> arraignmentAndRecords = police_casetoarraignmentMapper.getArraignmentByCaseSsid(ewarraignment);
                if (null!=arraignmentAndRecords&&arraignmentAndRecords.size()>0){
                    case_.setArraignments(arraignmentAndRecords);
                }

                //2、多个案件提讯人
                EntityWrapper ewuserinfo=new EntityWrapper<>();
                ewuserinfo.eq("ctu.casessid",case_.getSsid());
                List<UserInfo> userInfos=police_userinfoMapper.getUserByCase(ewuserinfo);
                if (null!=userInfos&&userInfos.size()>0){
                    for (UserInfo userInfo : userInfos) {
                        EntityWrapper ewcard=new EntityWrapper<>();
                        ewcard.eq("u.ssid",userInfo.getSsid());
                        List<UserInfoAndCard> cards=police_userinfoMapper.getCardByUser(ewcard);
                        userInfo.setCards(cards);
                    }
                    case_.setUserInfos(userInfos);
                }

                if(StringUtils.isNotEmpty(case_.getCreator())){
                    //查出创建人的名称ew
                    Base_admininfo base_admininfo = new Base_admininfo();
                    base_admininfo.setSsid(case_.getCreator());
                    Base_admininfo admininfo = base_admininfoMapper.selectOne(base_admininfo);
                    case_.setCreatorname(admininfo.getUsername());
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
            ewarraignment.ne("r.recordbool",-1);//笔录状态不为删除状态
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
