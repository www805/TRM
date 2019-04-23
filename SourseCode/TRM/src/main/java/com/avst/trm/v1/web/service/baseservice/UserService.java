package com.avst.trm.v1.web.service.baseservice;

import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admintoroleMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.req.basereq.GetUserListParam;
import com.avst.trm.v1.web.vo.basevo.GetUserListVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserService extends BaseService {
    @Autowired
    private Base_admininfoMapper base_admininfoMapper;

    @Autowired
    private Base_admintoroleMapper base_admintoroleMapper;

    public void getUserList(RResult result,GetUserListParam param){
        GetUserListVO getUserListVO=new GetUserListVO();

        //请求参数组合
        EntityWrapper ew=new EntityWrapper();
        if (null!=param.getLoginaccount()){
            ew.like(true,"a.loginaccount",param.getLoginaccount());
        }
        if (StringUtils.isNotBlank(param.getUsername())){
            ew.like(true,"a.username",param.getUsername());
        }
        if (StringUtils.isNotBlank(param.getWorkunitssid())){
            ew.eq(true,"a.workunitssid",param.getWorkunitssid());
        }
        if (StringUtils.isNotBlank(param.getRolessid())){
            ew.eq(true,"ar.rolessid",param.getRolessid());
        }
        if (null!=param.getAdminbool()){
            ew.eq(true,"a.adminbool",param.getAdminbool());
        }
        if (null!=param.getAdminbool()){
            ew.eq(true,"a.adminbool",param.getAdminbool());
        }

       int count = base_admininfoMapper.countgetUserList(ew);
       param.setRecordCount(count);

       ew.orderBy("a.unitsort",true);
       ew.orderBy("a.lastlogintime",false);
       Page<AdminAndWorkunit> page=new Page<AdminAndWorkunit>(param.getCurrPage(),param.getPageSize());
       List<AdminAndWorkunit> list=base_admininfoMapper.getUserList(page,ew);
       getUserListVO.setPageparam(param);
       
       if (null!=list&&list.size()>0){
           //绑定角色
           for (AdminAndWorkunit adminAndWorkunit : list) {
               EntityWrapper ewrole=new EntityWrapper();
               ewrole.eq("ar.adminssid",adminAndWorkunit.getSsid());
               List<Base_role> roles = base_admintoroleMapper.getRolesByAdminSsid(ewrole);
               if (null!=roles&&roles.size()>0){
                   adminAndWorkunit.setRoles(roles);
               }
           }
           getUserListVO.setPagelist(list);
       }
       result.setData(getUserListVO);
       changeResultToSuccess(result);
       return;
    }
}
