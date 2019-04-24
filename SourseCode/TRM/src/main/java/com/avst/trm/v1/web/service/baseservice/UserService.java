package com.avst.trm.v1.web.service.baseservice;

import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admintorole;
import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admintoroleMapper;
import com.avst.trm.v1.common.datasourse.police.entity.Police_workunit;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_workunitMapper;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.req.basereq.ChangeboolUserParam;
import com.avst.trm.v1.web.req.basereq.GetUserListParam;
import com.avst.trm.v1.web.vo.basevo.GetUserListVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("userService")
public class UserService extends BaseService {
    private Gson gson = new Gson();

    @Autowired
    private Base_admininfoMapper base_admininfoMapper;

    @Autowired
    private Base_admintoroleMapper base_admintoroleMapper;

    @Autowired
    private Police_workunitMapper police_workunitMapper;

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
        //筛选剔除已删除用户：
        ew.ne(true,"a.adminbool",-1);

       int count = base_admininfoMapper.countgetUserList(ew);
       param.setRecordCount(count);

       ew.orderBy("a.unitsort",true);
       ew.orderBy("a.registertime",false);
        ew.orderBy("a.lastlogintime",false);
       Page<AdminAndWorkunit> page=new Page<AdminAndWorkunit>(param.getCurrPage(),param.getPageSize());
       List<AdminAndWorkunit> list=base_admininfoMapper.getUserList(page,ew);
       getUserListVO.setPageparam(param);
       
       if (null!=list&&list.size()>0){
           //绑定状态正常的角色
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

    public void  getWorkunits(RResult result){
        try {
            List<Police_workunit> list=police_workunitMapper.selectList(null);
            if (null!=list&&list.size()>0){
                result.setData(list);
            }
            changeResultToSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void  changeboolUser(RResult result, ChangeboolUserParam param){
        String ssid=param.getSsid();
        Integer adminbool=param.getAdminbool();
        if (StringUtils.isBlank(ssid)||null==adminbool){
            result.setMessage("参数为空");
            return;
        }
        try {
            EntityWrapper ew=new EntityWrapper();
            ew.eq("ssid",ssid);
            Base_admininfo admininfo=new Base_admininfo();
            admininfo.setAdminbool(adminbool);
            int delete_bool= base_admininfoMapper.update(admininfo,ew);
            System.out.println("delete_bool__"+delete_bool);
            if (delete_bool<1){
                result.setMessage("系统异常");
                return;
            }
            result.setData(delete_bool);
            changeResultToSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void getUserBySsid(RResult result,String ssid){
        EntityWrapper ew=new EntityWrapper();
        ew.eq(true,"ssid",ssid);
        List<AdminAndWorkunit> list = base_admininfoMapper.selectList(ew);
        if (null==list||list.size()<1){
            result.setMessage("未找到该用户");
            return;
        }
        //查询角色
        if (null!=list&&list.size()>0){
            if (list.size()==1){
                AdminAndWorkunit adminAndWorkunit= gson.fromJson(gson.toJson(list.get(0)), AdminAndWorkunit.class);
                EntityWrapper ewrole=new EntityWrapper();
                ewrole.eq("ar.adminssid",adminAndWorkunit.getSsid());
                List<Base_role> roles = base_admintoroleMapper.getRolesByAdminSsid(ewrole);
                if (null!=roles&&roles.size()>0){
                    adminAndWorkunit.setRoles(roles);
                }
                result.setData(adminAndWorkunit);
                changeResultToSuccess(result);
            }else{
                System.out.println("系统异常：多个用户");
                result.setMessage("系统异常");
                return;
            }
        }
        return;
    }

    public void addUser(RResult result,AdminAndWorkunit param){
        try {
            //新增用户
            param.setSsid(OpenUtil.getUUID_32());
            param.setRegistertime(new Date());
            Base_admininfo admininfo = gson.fromJson(gson.toJson(param), Base_admininfo.class);
            int insert_bool=base_admininfoMapper.insert(admininfo);
            System.out.println("insert_bool__"+insert_bool);
            if (insert_bool>0){
                //添加角色关联数据
                List<Base_role> roles=param.getRoles();
                if (null!=roles&&roles.size()>0){
                    for (Base_role role : roles) {
                        Base_admintorole admintorole=new Base_admintorole();
                        admintorole.setAdminssid(admininfo.getSsid());
                        admintorole.setRolessid(role.getSsid());
                        admintorole.setCreatetime(new Date());
                       int admintorole_insertbool = base_admintoroleMapper.insert(admintorole);
                        System.out.println("admintorole_insertbool__"+admintorole_insertbool+"添加的角色为："+role.getRolename());
                    }
                }
                result.setData(insert_bool);
                changeResultToSuccess(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void updateUser(RResult result,AdminAndWorkunit param) {
        //新增用户
        EntityWrapper ew=new EntityWrapper();

        if (StringUtils.isBlank(param.getSsid())){
            result.setMessage("参数为空");
            return;
        }
        ew.eq("ssid",param.getSsid());
        param.setUpdatetime(new Date());
        Base_admininfo admininfo = gson.fromJson(gson.toJson(param), Base_admininfo.class);
        int update_bool = base_admininfoMapper.update(admininfo,ew);
        System.out.println("update_bool__" + update_bool);
        if (update_bool > 0) {
            //删除原有角色关联数据
            EntityWrapper ewadmintorole=new EntityWrapper();
            ewadmintorole.eq("adminssid",param.getSsid());
            int delete_bool=base_admintoroleMapper.delete(ewadmintorole);
            System.out.println("delete_bool__"+delete_bool);
            //添加角色关联数据
            List<Base_role> roles = param.getRoles();
            if (null != roles && roles.size() > 0) {
                for (Base_role role : roles) {
                    Base_admintorole admintorole = new Base_admintorole();
                    admintorole.setAdminssid(admininfo.getSsid());
                    admintorole.setRolessid(role.getSsid());
                    admintorole.setCreatetime(new Date());
                    int admintorole_insertbool = base_admintoroleMapper.insert(admintorole);
                    System.out.println("admintorole_insertbool__" + admintorole_insertbool + "添加的角色为：" + role.getRolename());
                }
            }

            result.setData(update_bool);
            changeResultToSuccess(result);
            return;
        }
    }

}
