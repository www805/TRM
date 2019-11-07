package com.avst.trm.v1.web.sweb.service.policeservice;

import com.avst.trm.v1.common.cache.Constant;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.police.entity.Police_workunit;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_workunitMapper;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.web.sweb.req.basereq.AddOrUpdateWorkunitParam;
import com.avst.trm.v1.web.sweb.req.basereq.GetworkunitListParam;
import com.avst.trm.v1.web.sweb.vo.basevo.GetworkunitListVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("workunitService")
public class WorkunitService  extends BaseService {
    @Autowired
    private Police_workunitMapper police_workunitMapper;

    public void getworkunitList(RResult result, GetworkunitListParam param){
        GetworkunitListVO vo=new GetworkunitListVO();

        EntityWrapper ew=new EntityWrapper();
        if (StringUtils.isNotBlank(param.getWorkname())){
            ew.like("workname",param.getWorkname().trim());
        }

        int count=police_workunitMapper.selectCount(ew);
        param.setRecordCount(count);

        ew.orderBy("createtime",false);
        Page<Police_workunit> page=new Page<Police_workunit>(param.getCurrPage(),param.getPageSize());
        List<Police_workunit> list=police_workunitMapper.selectPage(page,ew);
        vo.setPageparam(param);

        if(null!=list&&list.size() > 0){
            vo.setPagelist(list);
        }
        result.setData(vo);
        changeResultToSuccess(result);
        return;
    }

    public void addWorkunit(RResult result, AddOrUpdateWorkunitParam param){
        String workname=param.getWorkname();
        if (StringUtils.isBlank(workname)){
            result.setMessage("请输入工作单位名称");
            return;
        }

        EntityWrapper ew=new EntityWrapper();
        ew.eq("workname",workname);
        List<Police_workunit> base_roles_=police_workunitMapper.selectList(ew);
        if (null!=base_roles_&&base_roles_.size()>0){
            result.setMessage("工作单位名称已存在");
            return;
        }
        Gson gson = new Gson();
        Police_workunit workunit = gson.fromJson(gson.toJson(param), Police_workunit.class);
        workunit.setCreatetime(new Date());
        workunit.setSsid(OpenUtil.getUUID_32());
        workunit.setWorkname(workname);
        int insert_bool=police_workunitMapper.insert(workunit);
        LogUtil.intoLog(1,this.getClass(),"police_workunitMapper__insert_bool--"+insert_bool);
        if (insert_bool<1){
            result.setMessage("系统异常");
            return;
        }
        result.setData(insert_bool);
        changeResultToSuccess(result);
        return;
    }

    public void updateWorkunit(RResult result, AddOrUpdateWorkunitParam param){
        String ssid=param.getSsid();
        if (StringUtils.isBlank(ssid)){
            result.setMessage("未找到该工作单位");
            return;
        }

        String workname=param.getWorkname();
        if (StringUtils.isBlank(workname)){
            result.setMessage("请输入工作单位名称");
            return;
        }

        EntityWrapper ew=new EntityWrapper();
        ew.eq("workname",workname);
        ew.ne("ssid",ssid);
        List<Police_workunit> base_roles_=police_workunitMapper.selectList(ew);
        if (null!=base_roles_&&base_roles_.size()>0){
            result.setMessage("工作单位名称已存在");
            return;
        }
        Gson gson = new Gson();
        Police_workunit workunit = gson.fromJson(gson.toJson(param), Police_workunit.class);
        workunit.setCreatetime(new Date());
        workunit.setSsid(OpenUtil.getUUID_32());
        workunit.setWorkname(workname);

        EntityWrapper ew2=new EntityWrapper();
        ew2.eq(true,"ssid",ssid);
        int update_bool=police_workunitMapper.update(workunit,ew2);
        LogUtil.intoLog(1,this.getClass(),"police_workunitMapper__update_bool--"+update_bool);
        if (update_bool<1){
            result.setMessage("系统异常");
            return;
        }
        result.setData(update_bool);
        changeResultToSuccess(result);
        return;
    }

}
