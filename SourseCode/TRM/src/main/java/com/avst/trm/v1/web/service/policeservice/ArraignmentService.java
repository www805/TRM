package com.avst.trm.v1.web.service.policeservice;

import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtemplate;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.ArraignmentAndRecord;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.CaseAndUserInfo;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_caseMapper;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_casetoarraignmentMapper;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_recordMapper;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_recordtemplateMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.policereq.GetArraignmentListParam;
import com.avst.trm.v1.web.req.policereq.GetArraignment_countListParam;
import com.avst.trm.v1.web.vo.policevo.GetArraignmentBySsidVO;
import com.avst.trm.v1.web.vo.policevo.GetArraignmentListVO;
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
    private Police_recordtemplateMapper police_recordtemplateMapper;

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

    public void getArraignmentByCaseSsid(RResult result, Integer caseSsid){
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

    public void  getArraignmentBySsid(RResult result, Integer ssid){
        //查询笔录信息、案件信息、提讯信息、实时文件、全部录音文件
        GetArraignmentBySsidVO getArraignmentBySsidVO=new GetArraignmentBySsidVO();

        //全部模板
        List<Police_recordtemplate>  recordtemplates=police_recordtemplateMapper.selectList(null);
        if (null!=recordtemplates&&recordtemplates.size()>0){
            getArraignmentBySsidVO.setRecordtemplates(recordtemplates);
        }

        //笔录模板题目获取






        //笔录录音文件




        result.setData(getArraignmentBySsidVO);
        changeResultToSuccess(result);
        return;
    }


    public void getArraignment_countList(RResult result, GetArraignment_countListParam param){


        return;
    }
}
